package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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
		// bindingResult는 본인이 검증할 대상이 무엇인지 알고 있음
		log.info("objectName={} target={}", bindingResult.getObjectName(), bindingResult.getTarget());

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
			bindingResult.rejectValue("name", "required");
		}
		// ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "name", "required");
		// 위 if문이 이 코드와 동일 (단순 기능만 있음)

		if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
			bindingResult.rejectValue("price", "range", new Object[] {1000, 1000000}, null);
		}
		if (item.getQuantity() == null || item.getQuantity() < 1 || item.getQuantity() > 9999) {
			bindingResult.rejectValue("quantity", "range", new Object[] {1, 9999}, null);
		}

		// 복합 룰 검증
		if (item.getPrice() != null && item.getQuantity() != null) {
			long value = (long)item.getPrice() * item.getQuantity();
			if (value < 10000L) {
				bindingResult.reject("totalPriceMin", new Object[] {10000, value}, null);
			}
		}
	}
}

