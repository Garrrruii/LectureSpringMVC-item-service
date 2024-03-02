package hello.itemservice.web.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.itemservice.domain.item.Item;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

	@PostMapping("/add")
	public Object addItem(@RequestBody @Validated ItemSaveForm form,
		BindingResult bindingResult) {
		Item item = Item.builder()
			.name(form.getName()).price(form.getPrice()).quantity(form.getQuantity())
			.build();

		validateItemObjectError(item, bindingResult);
		if (bindingResult.hasErrors()) {
			log.info("{}", bindingResult);
			return bindingResult.getAllErrors();
		}

		return item;
	}

	@PostMapping("/{itemId}/edit")
	public Object edit(@PathVariable Long itemId,
		@RequestBody @Validated ItemUpdateForm form, BindingResult bindingResult) {
		Item item = Item.builder()
			.id(itemId).name(form.getName()).price(form.getPrice()).quantity(form.getQuantity())
			.build();

		validateItemObjectError(item, bindingResult);
		if (bindingResult.hasErrors()) {
			log.info("{}", bindingResult);
			return bindingResult.getAllErrors();
		}

		return item;
	}

	private void validateItemObjectError(Item item, BindingResult bindingResult) {
		if (item.getPrice() != null && item.getQuantity() != null) {
			long value = (long)item.getPrice() * item.getQuantity();
			if (value < 10000L) {
				bindingResult.reject("totalPriceMin", new Object[] {10000, value}, null);
			}
		}
	}
}

