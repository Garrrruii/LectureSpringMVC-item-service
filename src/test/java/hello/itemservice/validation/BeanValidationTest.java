package hello.itemservice.validation;

import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import hello.itemservice.domain.item.Item;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@SpringBootTest
class BeanValidationTest {

	@Test
	void messageCodesResolverObject() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Item item = Item.builder().name("").price(0).quantity(10000).build();

		Set<ConstraintViolation<Item>> violations = validator.validate(item);
		for (ConstraintViolation<Item> v : violations) {
			System.out.println(v);
		}

		Assertions.assertThat(violations.size()).isEqualTo(3);
	}

}
