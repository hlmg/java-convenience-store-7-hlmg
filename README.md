# java-convenience-store-precourse

## 구현할 기능 목록

### 초기화

- [x] 상품 목록 불러오는 기능
    - `src/main/resources/products.md` 파일 이용하기
    - 내용의 형식을 유지한다면 값은 수정해도 된다.
- [x] 프로모션 목록 불러오는 기능
    - `src/main/resources/promotions.md`
    - 내용의 형식을 유지한다면 값은 수정해도 된다.

### 주문 받기

- [ ] 환영 인사 출력하는 기능

- [ ] 판매 상품 출력하는 기능
    - 상품명, 가격, 프로모션 이름, 재고를 출력한다.
    - 재고가 0개라면 `재고 없음`을 출력한다.

- [ ] 구매할 상품과 수량을 입력받는 기능
    - 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
        - e.g. `[콜라-10],[사이다-3]`
    - 잘못된 값을 입력하면, `[error]`로 시작하는 오류메세지와 함께 안내 메세지를 출력한다.
        - 구매할 상품과 수량 형식이 올바르지 않은 경우:`[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`
        - 존재하지 않는 상품을 입력한 경우:`[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`
        - 구매 수량이 재고 수량을 초과한 경우:`[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`
        - 기타 잘못된 입력의 경우:`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`

### 주문 진행

- [x] 주문 상품이 구매 가능한지 확인하는 기능
    - 존재하지 않는 상품이면 예외가 발생한다.
    - 재고가 부족하면 예외가 발생한다.

- [x] 주문에 적용되는 프로모션을 조회하는 기능
    - 오늘 날짜가 프로모션 기간에 포함되어야 한다.
    - 프로모션 적용 대상이면, 프로모션 구매를 진행한다.
    - 프로모션 적용 대상이 아니면, 일반 구매를 진행한다.

- [x] 프로모션 주문을 진행하는 기능
    - 프로모션 상품 구매 수량을 확인할 수 있다.
    - 증정 상품 수량을 확인할 수 있다.
    - 보류중인 상품 수량을 확인할 수 있다.
        - 프로모션이 부분 적용된 경우, 프로모션 미적용 상품은 구매가 보류된다.
        - 증정 상품을 추가할 수 있는 경우, 프로모션 미적용 상품은 구매가 보류된다.
    - 증정 상품을 추가할 수 있는지 확인할 수 있다.
    - 프로모션 재고 부족 여부를 학인할 수 있다.

- [ ] 증정 상품 추가 여부를 입력받는 기능
    - 구매 결과가 증정 상품을 추가할 수 있는 경우 추가 입력을 받는다.
    - Y: 증정 받을 수 있는 상품을 추가한다.
    - N: 증정 받을 수 있는 상품을 추가하지 않는다.

- [x] 증정 상품 추가 여부를 처리하는 기능
    - 증정 상품 추가 여부를 처리할 수 없는 타입이면 예외가 발생한다.
    - 증정 상품 추가 여부를 처리할 수 없는 상태면 예외가 발생한다.

- [ ] 정가 결제 여부를 입력받는 기능
    - 프로모션이 부분적용된 경우 추가 입력을 받는다.
    - Y: 일부 수량에 대해 정가로 결제한다.
    - N: 정가로 결제해야하는 수량만큼 제외한 후 결제를 진행한다.

- [x] 정가 결제 여부를 처리하는 기능
    - 정가 결제 여부를 처리할 수 없는 타입이면 예외가 발생한다.
    - 정가 결제 여부를 처리할 수 없는 상태면 예외가 발생한다.

- [x] 일반 주문을 진행하는 기능

### 멤버십 적용

- [ ] 멤버십 할인 적용 여부를 입력 받는 기능
    - Y: 멤버십 할인을 적용한다.
    - N: 멤버십 할인을 적용하지 않는다.

- [x] 멤버십 할인 금액을 계산하는 기능
    - 프로모션 미적용 금액의 30%를 할인한다.
    - 할인액은 일의 자리에서 반올림한다.
    - 멤버십 할인의 최대 한도는 8,000원이다.

### 주문 완료

- [ ] 금액 정보 계산하는 기능
  - 총구매액, 행사할인 금액, 멤버십할인 금액, 결제 금액을 계산할 수 있다.

- [ ] 영수증 출력하는 기능
  - 구매 상품 내역, 증정 상품 내역, 금액 정보를 출력한다.

- [ ] 재고 차감하는 기능
    - 프로모션 대상이면, 프로모션 재고를 먼저 차감한다.

- [ ] 추가 구매 여부를 입력 받는 기능
    - Y: 재고가 업데이트된 상품 목록을 확인 후 추가로 구매를 진행한다.
    - N: 구매를 종료한다.

---

출력 예시

```text
안녕하세요. W편의점입니다.
현재 보유하고 있는 상품입니다.

- 콜라 1,000원 7개 탄산2+1
- 콜라 1,000원 10개
- 사이다 1,000원 8개 탄산2+1
- 사이다 1,000원 7개
- 오렌지주스 1,800원 9개 MD추천상품
- 오렌지주스 1,800원 재고 없음
- 탄산수 1,200원 5개 탄산2+1
- 탄산수 1,200원 재고 없음
- 물 500원 10개
- 비타민워터 1,500원 6개
- 감자칩 1,500원 5개 반짝할인
- 감자칩 1,500원 5개
- 초코바 1,200원 5개 MD추천상품
- 초코바 1,200원 5개
- 에너지바 2,000원 재고 없음
- 정식도시락 6,400원 8개
- 컵라면 1,700원 1개 MD추천상품
- 컵라면 1,700원 10개

구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])
[콜라-10]

현재 콜라 4개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)
Y

멤버십 할인을 받으시겠습니까? (Y/N)
N

===========W 편의점=============
상품명		수량	금액
콜라		10 	10,000
===========증	정=============
콜라		2
==============================
총구매액		10	10,000
행사할인			-2,000
멤버십할인			-0
내실돈			 8,000
```
