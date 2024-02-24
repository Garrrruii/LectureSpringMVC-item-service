package hello.itemservice.domain.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {

	private Long id;
	private String name;
	private Integer price;
	private Integer quantity;

}
