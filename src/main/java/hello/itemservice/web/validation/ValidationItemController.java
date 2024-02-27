package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/items") // v2
@RequiredArgsConstructor
public class ValidationItemController {

	private final ItemRepository itemRepository;

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);
		return "validation/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/item";
	}

	@GetMapping("/add")
	public String addForm(Model model) {
		model.addAttribute("item", Item.builder().build());
		return "validation/addForm";
	}

	@PostMapping("/add")
	public String addItem(@ModelAttribute Item item, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
		// item과 bindingResult의 순서가 중요 (item에 바인딩 실패 시 bindingResult에 FieldError를 담음)
		validateItem(item, bindingResult);
		if (bindingResult.hasErrors()) {
			log.info("{}", bindingResult);
			return "validation/addForm";
		}

		Item savedItem = itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/validation/items/{itemId}";
	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "validation/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
		itemRepository.update(itemId, item);
		return "redirect:/validation/items/{itemId}";
	}

	private static void validateItem(Item item, BindingResult bindingResult) {
		// 필드 검증
		if (!StringUtils.hasText(item.getName())) {
			bindingResult.addError(new FieldError(
				"item", "name", item.getName(), false, null, null, "상품명은 필수입니다."));
		}
		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			bindingResult.addError(new FieldError(
				"item", "price", item.getPrice(), false, null, null, "가격은 1,000~1,000,000까지 허용합니다."));
		}
		if (item.getQuantity() == null || item.getQuantity() < 0 || item.getQuantity() > 9999) {
			bindingResult.addError(new FieldError(
				"item", "quantity", item.getQuantity(), false, null, null, "수량은 1~9,999까지 허용합니다."));
		}

		// 복합 룰 검증
		if (item.getPrice() != null && item.getQuantity() != null) {
			long value = (long)item.getPrice() * item.getQuantity();
			if (value < 10000L) {
				bindingResult.addError(new ObjectError("item", "가격*수량은 10,000 이상이어야 합니다. 현재값 = " + value));
			}
		}
	}
}

