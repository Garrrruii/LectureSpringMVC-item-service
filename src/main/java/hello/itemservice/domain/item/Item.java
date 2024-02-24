package hello.itemservice.domain.item;

import lombok.Data;

@Data
public class Item {

	private Long id;
	private String name;
	private Integer price;
	private Integer quantity;

}
