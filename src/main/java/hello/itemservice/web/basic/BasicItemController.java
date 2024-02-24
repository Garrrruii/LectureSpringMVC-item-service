package hello.itemservice.web.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

	private final ItemRepository itemRepository;

	@GetMapping
	public String items(Model model) {
		List<Item> items = itemRepository.findAll();
		model.addAttribute("items", items);

		return "basic/items";
	}

	@GetMapping("/{itemId}")
	public String item(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/item";
	}

	@GetMapping("/add")
	public String addForm() {
		return "basic/addForm";
	}

	// 동일 url을 http method로 기능 구분
	@PostMapping("/add")
	public String addItem(@ModelAttribute("item") Item item) {
		// 1. @ModelAttribute 사용 => Item 을 직접 생성하지 않아도 된다.
		// 2. @ModelAttribute 이름 지정 => model.addAttribute() 생략 가능
		// 3. @ModelAttribute 이름 생략 시, 클래스 명에 smallCamelCase 를 적용한 이름으로 model.addAttribute된다.
		// 4. (remark) 사실 어노테이션 자체도 생략 가능

		itemRepository.save(item);
		return "basic/item";
	}

	@GetMapping("/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model) {
		Item item = itemRepository.findById(itemId);
		model.addAttribute("item", item);
		return "basic/editForm";
	}

	@PostMapping("/{itemId}/edit")
	public String editItem(@PathVariable Long itemId, @ModelAttribute("item") Item updatedItem, Model model) {
		itemRepository.update(itemId, updatedItem);

		return "redirect:/basic/items/{itemId}";
		// 1. redirect
		// 2. PathVariable로 사용한 변수는 이렇게도 사용 가능
	}

	@PostConstruct
	public void init() {
		itemRepository.save(Item.builder().name("itemA").price(10000).quantity(10).build());
		itemRepository.save(Item.builder().name("itemB").price(20000).quantity(20).build());
	}

}
