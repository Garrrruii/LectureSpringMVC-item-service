package hello.itemservice.message;

import static org.assertj.core.api.Assertions.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

@SpringBootTest
class MessageSourceTests {

	@Autowired
	MessageSource ms;

	@Test
	void helloMessage() {
		String helloDef = ms.getMessage("hello", null, null);
		assertThat(helloDef).isEqualTo("안녕");

		// Locale
		String helloKr = ms.getMessage("hello", null, Locale.KOREA);
		assertThat(helloKr).isEqualTo(helloDef);

		String helloEn = ms.getMessage("hello", null, Locale.ENGLISH);
		assertThat(helloEn).isEqualTo("Hello");

		// args
		String helloSpringDef = ms.getMessage("hello.name", new Object[] {"Spring"}, null);
		assertThat(helloSpringDef).isEqualTo("안녕 Spring");
	}

	@Test
	void nonFoundMessageCode() {
		assertThatThrownBy(() -> ms.getMessage("no-code", null, null)).isInstanceOf(NoSuchMessageException.class);
	}

	@Test
	void notFoundMessageCodeDefaultMessage() {
		String result = ms.getMessage("no-code", null, "기본 메세지", null);
		assertThat(result).isEqualTo("기본 메세지");
	}

}
