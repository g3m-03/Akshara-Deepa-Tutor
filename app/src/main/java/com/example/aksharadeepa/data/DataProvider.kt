package com.example.aksharadeepa.data

data class Subject(val name: String, val chapters: List<String>)

object DataProvider {
    val subjects = listOf(
        Subject("Science", (1..16).map { "Chapter $it" }),
        Subject("Mathematics", (1..15).map { "Chapter $it" }),
        Subject("Social Studies", (1..12).map { "Chapter $it" })
    )

    val quizzes = mapOf(
        "Science" to listOf(
            Question("What is the chemical symbol for water?", listOf("H2O", "CO2", "O2", "NaCl"), 0),
            Question("Which planet is known as the Red Planet?", listOf("Earth", "Mars", "Jupiter", "Venus"), 1),
            Question("What is the hardest natural substance on Earth?", listOf("Gold", "Iron", "Diamond", "Platinum"), 2),
            Question("What gas do plants absorb from the atmosphere?", listOf("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"), 2),
            Question("Who proposed the theory of relativity?", listOf("Newton", "Galileo", "Einstein", "Bohr"), 2)
        ),
        "Mathematics" to listOf(
            Question("What is the value of Pi to two decimal places?", listOf("3.14", "3.16", "3.12", "3.18"), 0),
            Question("What is the square root of 144?", listOf("10", "12", "14", "16"), 1),
            Question("What is 15% of 200?", listOf("20", "30", "40", "50"), 1),
            Question("If 2x = 10, what is x?", listOf("2", "4", "5", "10"), 2),
            Question("What is the next prime number after 7?", listOf("8", "9", "10", "11"), 3)
        ),
        "Social Studies" to listOf(
            Question("Who was the first President of India?", listOf("Dr. Rajendra Prasad", "Jawaharlal Nehru", "Mahatma Gandhi", "B.R. Ambedkar"), 0),
            Question("Which is the longest river in the world?", listOf("Amazon", "Nile", "Yangtze", "Mississippi"), 1),
            Question("In which year did India gain independence?", listOf("1945", "1946", "1947", "1948"), 2),
            Question("What is the capital of Australia?", listOf("Sydney", "Melbourne", "Canberra", "Perth"), 2),
            Question("Who wrote the Indian National Anthem?", listOf("Bankim Chandra Chatterjee", "Rabindranath Tagore", "Sarojini Naidu", "Subhas Chandra Bose"), 1)
        )
    )
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
