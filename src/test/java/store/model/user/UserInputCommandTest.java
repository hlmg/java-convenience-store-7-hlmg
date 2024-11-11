package store.model.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import store.exception.StoreException;
import store.model.user.UserInputCommand;

@SuppressWarnings("NonAsciiCharacters")
class UserInputCommandTest {

    @CsvSource(textBlock = """
            Y,YES
            N,NO
            """)
    @ParameterizedTest
    void 유효한_심볼로_커맨드를_찾을_수_있다(String symbol, UserInputCommand expected) {
        // when
        UserInputCommand find = UserInputCommand.from(symbol);

        // then
        assertThat(find).isSameAs(expected);
    }

    @NullAndEmptySource
    @ValueSource(strings = {"y", "A"})
    @ParameterizedTest
    void 커맨드를_찾을_수_없으면_예외가_발생한다(String invalidSymbol) {
        // when & then
        assertThatThrownBy(() -> UserInputCommand.from(invalidSymbol))
                .isInstanceOf(StoreException.class)
                .hasMessage("잘못된 입력입니다.");
    }

}
