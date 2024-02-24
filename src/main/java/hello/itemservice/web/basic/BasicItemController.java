package hello.itemservice.web.basic;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String addItem(@RequestParam String itemName, @RequestParam Integer price, @RequestParam Integer quantity,
		Model model) {
		Item item = Item.builder().name(itemName).price(price).quantity(quantity).build();

		itemRepository.save(item);
		model.addAttribute("item", item);
		return "basic/item";
	}

	@PostConstruct
	public void init() {
		itemRepository.save(Item.builder().name("itemA").price(10000).quantity(10).build());
		itemRepository.save(Item.builder().name("itemB").price(20000).quantity(20).build());
	}

}
