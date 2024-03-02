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

	@NotNull(groups = UpdateCheck.class)
	private Long id;

	@NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
	private String name;

	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
	@Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
	private Integer price;

	@NotNull(groups = {SaveCheck.class, UpdateCheck.class})
	@Max(value = 9999, groups = SaveCheck.class)
	private Integer quantity;

	private Boolean open;
	private List<String> regions;
	private ItemType itemType;
	private String deliveryCode;

}
