package hello.itemservice.domain.item;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

	private Long id;
	private String name;
	private Integer price;
	private Integer quantity;

	private Boolean open;
	private List<String> regions;
	private ItemType itemType;
	private String deliveryCode;

}
