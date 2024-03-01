package hello.itemservice.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return Item.class.isAssignableFrom(clazz); // clazz 및 자식 클래스도 검증 가능
	}

	@Override
	public void validate(Object target, Errors errors) {
		try {
			Item item = (Item)target;

			// 필드 검증
			if (!StringUtils.hasText(item.getName())) {
				errors.rejectValue("name", "required");
			}
			if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
				errors.rejectValue("price", "range", new Object[] {1000, 1000000}, null);
			}
			if (item.getQuantity() == null || item.getQuantity() < 1 || item.getQuantity() > 9999) {
				errors.rejectValue("quantity", "range", new Object[] {1, 9999}, null);
			}

			// 복합 룰 검증
			if (item.getPrice() != null && item.getQuantity() != null) {
				long value = (long)item.getPrice() * item.getQuantity();
				if (value < 10000L) {
					errors.reject("totalPriceMin", new Object[] {10000, value}, null);
				}
			}
		} catch (Exception e) {
			log.error("validateItem exception", e);
		}
	}

}
