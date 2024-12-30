import java.util.ArrayList;

/**
 * Клас, що представляє лiтеру.
 */
class Letter {
    private char value;

    /**
     * Конструктор для створення лiтери.
     *
     * @param value значення лiтери
     */
    public Letter(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Клас, що представляє роздiловий знак.
 */
class Punctuation {
    private char value;

    /**
     * Конструктор для створення роздiлового знаку.
     *
     * @param value значення роздiлового знаку
     */
    public Punctuation(char value) {
        this.value = value;
    }

    public char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Клас, що представляє слово як масив лiтер.
 */
class Word {
    private Letter[] letters;

    /**
     * Конструктор для створення слова з рядка.
     *
     * @param word рядок для створення слова
     */
    public Word(String word) {
        letters = new Letter[word.length()];
        for (int i = 0; i < word.length(); i++) {
            letters[i] = new Letter(word.charAt(i));
        }
    }

    /**
     * Перевiряє, чи починається слово з певної лiтери.
     *
     * @param c лiтера для перевiрки
     * @return true, якщо слово починається з цiєї лiтери
     */
    public boolean startsWith(char c) {
        return letters.length > 0 && 
               Character.toLowerCase(letters[0].getValue()) == Character.toLowerCase(c);
    }

    /**
     * Перевiряє, чи закiнчується слово певною лiтерою.
     *
     * @param c лiтера для перевiрки
     * @return true, якщо слово закiнчується цiєю лiтерою
     */
    public boolean endsWith(char c) {
        return letters.length > 0 && 
               Character.toLowerCase(letters[letters.length - 1].getValue()) == 
               Character.toLowerCase(c);
    }

    public int length() {
        return letters.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Letter letter : letters) {
            sb.append(letter.toString());
        }
        return sb.toString();
    }
}

/**
 * Клас, що представляє речення як масив слiв та роздiлових знакiв.
 */
class Sentence {
    private ArrayList<Object> elements; // Може мiстити Word або Punctuation

    /**
     * Конструктор для створення речення.
     */
    public Sentence() {
        elements = new ArrayList<>();
    }

    /**
     * Додає слово до речення.
     *
     * @param word слово для додавання
     */
    public void addWord(Word word) {
        elements.add(word);
    }

    /**
     * Додає роздiловий знак до речення.
     *
     * @param punctuation роздiловий знак для додавання
     */
    public void addPunctuation(Punctuation punctuation) {
        elements.add(punctuation);
    }

    /**
     * Видаляє найдовший пiдрядок мiж заданими символами.
     *
     * @param startChar початковий символ
     * @param endChar кiнцевий символ
     */
    public void removeLongestSubstring(char startChar, char endChar) {
        int maxLength = -1;
        int startIndex = -1;
        int endIndex = -1;
        StringBuilder currentString = new StringBuilder();
        ArrayList<Integer> wordIndexes = new ArrayList<>();

        // Створюємо тимчасовий рядок i зберiгаємо iндекси слiв
        for (int i = 0; i < elements.size(); i++) {
            Object element = elements.get(i);
            if (element instanceof Word) {
                wordIndexes.add(currentString.length());
                currentString.append(element.toString());
            }
            if (element instanceof Punctuation) {
                currentString.append(element.toString());
            }
        }

        // Шукаємо найдовший пiдрядок
        String str = currentString.toString();
        for (int i = 0; i < str.length(); i++) {
            if (Character.toLowerCase(str.charAt(i)) == Character.toLowerCase(startChar)) {
                for (int j = i + 1; j < str.length(); j++) {
                    if (Character.toLowerCase(str.charAt(j)) == Character.toLowerCase(endChar)) {
                        int length = j - i + 1;
                        if (length > maxLength) {
                            maxLength = length;
                            startIndex = i;
                            endIndex = j + 1;
                        }
                    }
                }
            }
        }

        // Якщо знайдено пiдрядок, видаляємо вiдповiднi слова
        if (startIndex != -1) {
            int startWordIndex = -1;
            int endWordIndex = -1;

            // Знаходимо iндекси слiв, якi мiстять пiдрядок
            for (int i = 0; i < wordIndexes.size(); i++) {
                if (wordIndexes.get(i) <= startIndex && 
                    (i == wordIndexes.size() - 1 || wordIndexes.get(i + 1) > startIndex)) {
                    startWordIndex = i;
                }
                if (wordIndexes.get(i) < endIndex && 
                    (i == wordIndexes.size() - 1 || wordIndexes.get(i + 1) >= endIndex)) {
                    endWordIndex = i;
                }
            }

            // Видаляємо слова
            if (startWordIndex != -1 && endWordIndex != -1) {
                for (int i = endWordIndex; i >= startWordIndex; i--) {
                    elements.remove(i);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean firstWord = true;
        
        for (Object element : elements) {
            if (element instanceof Word) {
                if (!firstWord) {
                    sb.append(" ");
                }
                firstWord = false;
            }
            sb.append(element.toString());
        }
        return sb.toString();
    }
}

/**
 * Клас, що представляє текст як масив речень.
 */
class Text {
    private ArrayList<Sentence> sentences;

    /**
     * Конструктор для створення тексту.
     */
    public Text() {
        sentences = new ArrayList<>();
    }

    /**
     * Додає речення до тексту.
     *
     * @param sentence речення для додавання
     */
    public void addSentence(Sentence sentence) {
        sentences.add(sentence);
    }

    /**
     * Обробляє текст, видаляючи найдовшi пiдрядки в кожному реченнi.
     *
     * @param startChar початковий символ
     * @param endChar кiнцевий символ
     */
    public void processText(char startChar, char endChar) {
        for (Sentence sentence : sentences) {
            sentence.removeLongestSubstring(startChar, endChar);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Sentence sentence : sentences) {
            sb.append(sentence.toString()).append(" ");
        }
        return sb.toString().trim().replaceAll("\\s+", " ");
    }
}

/**
 * Головний клас для обробки тексту.
 */
public class TextProcessor {
    /**
     * Парсить рядок у структуру тексту.
     *
     * @param input вхiдний текст
     * @return об'єкт Text
     */
    private static Text parseText(String input) {
        Text text = new Text();
        String[] sentenceStrings = input.split("[.!?]");
        
        for (String sentenceStr : sentenceStrings) {
            if (sentenceStr.trim().isEmpty()) {
                continue;
            }
            
            Sentence sentence = new Sentence();
            String[] words = sentenceStr.trim().split("\\s+");
            
            for (String word : words) {
                if (!word.isEmpty()) {
                    // Роздiляємо слово та пунктуацiю
                    String[] parts = word.split("(?<=\\w)(?=\\p{Punct})|(?<=\\p{Punct})(?=\\w)");
                    for (String part : parts) {
                        if (part.matches(".*\\w.*")) {
                            sentence.addWord(new Word(part));
                        } else {
                            for (char c : part.toCharArray()) {
                                sentence.addPunctuation(new Punctuation(c));
                            }
                        }
                    }
                }
            }
            text.addSentence(sentence);
        }
        return text;
    }

    /**
     * Виконавчий метод програми.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        try {
            // Вхiдний текст
            String inputText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
                    + "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
            
            // Заданi лiтери для пошуку пiдрядка
            char startChar = 'p';
            char endChar = 'i';

            System.out.println("Початковий текст: " + inputText);
            System.out.println("Шукаємо пiдрядки вiд '" + startChar + "' до '" + endChar + "'");

            // Обробка тексту
            Text text = parseText(inputText);
            text.processText(startChar, endChar);

            System.out.println("\nРезультат: " + text);

        } catch (Exception e) {
            System.err.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}