package hello.itemservice;

import org.springframework.stereotype.Component;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestDataInit {

	private final ItemRepository itemRepository;

	/**
	 * 테스트용 데이터 추가
	 */
	@PostConstruct
	public void init() {
		itemRepository.save(Item.builder().name("itemA").price(10000).quantity(10).build());
		itemRepository.save(Item.builder().name("itemB").price(20000).quantity(20).build());
	}

}