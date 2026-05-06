package eu.ibagroup.snippet_manager.enumeration;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DevLang {
    TEXT("Text"),
    JAVA("Java"),
    JS("JavaScript"),
    TS("TypeScript"),
    SQL("SQL"),
    PYTHON("Python"),
    HTML("HTML"),
    CSS("CSS"),
    C_SHARP("C#"),
    C_PLUS_PLUS("C++"),
    XML("XML"),
    JSON("JSON"),
    RUBY("Ruby"),
    GO("Go"),
    RUST("Rust");

    private final String languageName;

    DevLang(String displayName) {
        this.languageName = displayName;
    }

    public static DevLang fromLanguage(String language) {
        return Arrays.stream(DevLang.values())
                .filter(lang -> lang.languageName.equalsIgnoreCase(language))
                .findFirst()
                .orElse(null);
    }

}
