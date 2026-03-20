# QuizApp-Android Improvement Plan

This document outlines suggested improvements to enhance the maintainability, scalability, and performance of the QuizApp-Android project.

## 1. Data Management Refactoring
### Current Issue
Questions, options, and answers are currently stored as massive hardcoded `public static String[]` arrays in classes like `Part1QuestionList`. This approach is memory-intensive and makes content updates difficult.

### Proposed Solution
*   **Externalize Data:** Move quiz content into structured files (JSON, CSV, or XML) stored in the `assets/` folder.
*   **Database Integration:** For better performance and offline capabilities, migrate the data to a **Room Persistence Library** (SQLite) database.
*   **Firebase Synchronization:** Leverage the existing Firebase Realtime Database to fetch the latest questions dynamically.

## 2. Introduce a Domain Model
### Current Issue
The app uses parallel arrays to link questions, options, and answers, which is error-prone.

### Proposed Solution
*   **Question Class:** Create a dedicated `Question` data class to encapsulate all properties of a single question.
    ```java
    public class Question {
        private String id;
        private String text;
        private List<String> options;
        private String correctAnswer;
        private String category; // e.g., "Part 1", "Paper 1 SET1"
        // Getters and Setters
    }
    ```

## 3. Dynamic Question Loading
### Current Issue
`QuestionsActivity.java` uses a large `switch` statement with hardcoded index offsets (e.g., `flag = 100`) to select question sets. This is brittle and hard to maintain.

### Proposed Solution
*   **Filtering:** Query the database or filter the loaded JSON/CSV list based on the `part_name` and `paper_name` passed via Intents.
*   **Eliminate Offsets:** Stop using manual index offsets. Load exactly the questions needed for the selected set.

## 4. Modernize Architecture (MVVM)
### Current Issue
`QuestionsActivity` is overloaded with responsibilities (UI, Timer, Scoring, Data Logic). It also relies on `public static` variables for state, which can lead to bugs.

### Proposed Solution
*   **ViewModel:** Implement `QuestionsViewModel` to handle scoring logic, question traversal, and timer state.
*   **LiveData:** Use `LiveData` to observe changes in the current question and score, allowing the UI to update automatically.
*   **State Persistence:** Stop using `public static` for `marks`, `correct`, and `wrong`. Keep this state within the `ViewModel` to survive configuration changes (like rotation).

## 5. Improved Timer Implementation
### Current Issue
The `CountDownTimer` in `QuestionsActivity` restarts or behaves inconsistently on Activity lifecycle changes (e.g., `onResume`).

### Proposed Solution
*   **ViewModel-bound Timer:** Move the timer logic into the `ViewModel`. This ensures the timer continues running accurately even if the Activity is temporarily destroyed.

## 6. Code Quality & Formatting
*   **View Binding:** You have `viewBinding { enabled true }` in `build.gradle`. Ensure all Activities are migrated from `findViewById` to View Binding for type-safe view access.
*   **String Resources:** Move hardcoded UI strings and Toast messages into `strings.xml`.
*   **Package Structure:** Organize classes into sub-packages (e.g., `.ui`, `.data`, `.model`) to improve project navigation.
