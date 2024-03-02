package hello.itemservice.web.validation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/validation/items") // v4
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
	public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult,
		RedirectAttributes redirectAttributes) {
		Item item = Item.builder()
			.name(form.getName()).price(form.getPrice()).quantity(form.getQuantity())
			.build();

		validateItemObjectError(item, bindingResult);
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
	public String edit(@PathVariable Long itemId,
		@Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
		Item item = Item.builder()
			.id(itemId).name(form.getName()).price(form.getPrice()).quantity(form.getQuantity())
			.build();

		validateItemObjectError(item, bindingResult);
		if (bindingResult.hasErrors()) {
			log.info("{}", bindingResult);
			return "validation/editForm";
		}

		itemRepository.update(itemId, item);
		return "redirect:/validation/items/{itemId}";
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

