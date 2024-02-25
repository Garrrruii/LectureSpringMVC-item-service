package hello.itemservice.domain.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

	private static final Map<Long, Item> store = new HashMap<>();
	// 실무에서 여러 쓰레드가 여기 접근하는 경우  HashMap 사용하면 안 됨. ConcurrentMap 사용해야
	private static Long sequence = 0L;

	public Item save(Item item) {
		item.setId(++sequence);
		store.put(item.getId(), item);
		return item;
	}

	public Item findById(Long id) {
		return store.get(id);
	}

	public List<Item> findAll() {
		return new ArrayList<>(store.values());
	}

	public void update(Long itemId, Item updatedItem) {
		// update 시에는 update되는 필드만 뽑아서 새로 dto 등을 만드는 것이 적절함
		Item findItem = findById(itemId);
		findItem.setName(updatedItem.getName());
		findItem.setPrice(updatedItem.getPrice());
		findItem.setQuantity(updatedItem.getQuantity());
		findItem.setOpen(updatedItem.getOpen());
		findItem.setRegions(updatedItem.getRegions());
		findItem.setItemType(updatedItem.getItemType());
		findItem.setDeliveryCode(updatedItem.getDeliveryCode());
	}

	public void clearStore() {
		store.clear();
	}
}
