package hello.itemservice.domain.item;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTests {

	ItemRepository itemRepository = new ItemRepository();

	@AfterEach
	void afterEach() {
		itemRepository.clearStore();
	}

	@Test
	void save() {
		// given
		Item item = Item.builder().name("itemA").price(10000).quantity(10).build();

		// when
		Item savedItem = itemRepository.save(item);

		// then
		Item findItem = itemRepository.findById(item.getId());
		assertThat(findItem).isEqualTo(savedItem);
	}

	@Test
	void findAll() {
		// given
		Item itemA = Item.builder().name("itemA").price(10000).quantity(10).build();
		Item itemB = Item.builder().name("itemB").price(20000).quantity(20).build();

		// when
		itemRepository.save(itemA);
		itemRepository.save(itemB);

		// then
		List<Item> list = itemRepository.findAll();
		assertThat(list.size()).isEqualTo(2);
		assertThat(list).contains(itemA, itemB);
	}

	@Test
	void update() {
		// given
		Item item = Item.builder().name("itemA").price(10000).quantity(10).build();
		Item savedItem = itemRepository.save(item);
		Long itemId = savedItem.getId();

		// when
		Item updatedItem = Item.builder().name("itemB").price(20000).quantity(20).build();
		itemRepository.update(itemId, updatedItem);

		// then
		Item findItem = itemRepository.findById(item.getId());
		assertThat(findItem.getName()).isEqualTo(updatedItem.getName());
		assertThat(findItem.getPrice()).isEqualTo(updatedItem.getPrice());
		assertThat(findItem.getQuantity()).isEqualTo(updatedItem.getQuantity());
	}

}
