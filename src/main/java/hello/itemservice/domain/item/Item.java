package hello.itemservice.domain.item;

import java.util.List;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	private Long id;

	@NotBlank
	private String name;

	@NotNull
	@Range(min = 1000, max = 1000000)
	private Integer price;

	@NotNull
	@Max(9999)
	private Integer quantity;

	private Boolean open;
	private List<String> regions;
	private ItemType itemType;
	private String deliveryCode;

}
