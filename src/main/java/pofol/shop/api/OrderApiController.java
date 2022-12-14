package pofol.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Member;
import pofol.shop.domain.Order;
import pofol.shop.domain.OrderSheet;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.create.CreateOrderSheetForm;
import pofol.shop.repository.MemberRepository;
import pofol.shop.repository.OrderRepository;
import pofol.shop.repository.OrderSheetRepository;
import pofol.shop.service.business.MemberService;
import pofol.shop.service.business.OrderService;

/**
 * 주문과 관련된 API요청을 처리하는 Controller 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-10
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-28
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final OrderSheetRepository orderSheetRepository;
    private ObjectMapper mapper = new ObjectMapper(); //JSON - 객체 간 매퍼


    /**
     * 새로운 주문시트를 생성합니다.
     *
     * @param form 주문시트 생성에 필요한 데이터 폼
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-10
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-28
     */
    @PostMapping("/order-sheet") //주문시트 생성 요청
    public ResponseEntity<ApiResponseBody<Long>> createOrderSheet(@RequestBody CreateOrderSheetForm form) {

        try {
            OrderSheet sheet = new OrderSheet();
            Member member = memberService.findByUserName(form.getUserName());

            //Form을 JSON으로 변환해서 DB에 저장

            sheet.setContent(mapper.writeValueAsString(form));
            sheet.setMember(member);
            sheet.setIsOrdered(false);
            Long id = orderSheetRepository.save(sheet).getId();

            return ResponseEntity
                    .ok()
                    .body(new ApiResponseBody<>(HttpStatus.OK, "주문시트 생성 완료", id));


        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }

    }

    /**
     * 주문을 취소합니다.
     *
     * @param id 취소할 주문의 id
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-30
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-07
     */
    @PostMapping("/orders/{orderId}/cancel") //주문취소 요청
    public ResponseEntity<ApiResponseBody<String>> delete(@PathVariable("orderId") Long id,
                                                          @AuthenticationPrincipal UserAdapter principal) {

        Order order = orderService.findById((id));
        if (!order.getMember().getUserName().equals(principal.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ApiResponseBody<>(HttpStatus.FORBIDDEN, "주문을 한 회원이 아닙니다", null));
        }

        order.cancel();
        orderRepository.save(order);
        return ResponseEntity
                .ok()
                .body(new ApiResponseBody<>(HttpStatus.OK, "취소 성공", "/orders/cancel-finish"));

    }
}
