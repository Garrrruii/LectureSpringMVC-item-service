package hello.itemservice.web.validation.form;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSaveForm {

	@NotBlank
	private String name;

	@NotNull
	@Range(min = 1000, max = 1000000)
	private Integer price;

	@NotNull
	@Max(9999)
	private Integer quantity;
}
