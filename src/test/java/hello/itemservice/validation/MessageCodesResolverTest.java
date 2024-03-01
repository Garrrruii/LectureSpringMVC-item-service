package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

@SpringBootTest
class MessageCodesResolverTest {

	MessageCodesResolver cr = new DefaultMessageCodesResolver();

	@Test
	void messageCodesResolverObject() {
		String[] codes = cr.resolveMessageCodes("required", "item");

		for (String c : codes) {
			System.out.println(c);
		}
		Assertions.assertThat(codes).containsExactly("required.item", "required");
	}

	@Test
	void messageCodesResolverField() {
		String[] codes = cr.resolveMessageCodes("required", "item", "name", String.class);

		for (String c : codes) {
			System.out.println(c);
		}
		Assertions.assertThat(codes)
			.containsExactly("required.item.name", "required.name", "required.java.lang.String", "required");
	}

}
