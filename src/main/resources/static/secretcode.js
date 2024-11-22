// Tablica z kolorami do wyboru
const colors = ["white", "blue", "green", "yellow", "red", "black"];
let currentColorIndex = {};

// Inicjalizacja zmiany kolorów
document.querySelectorAll(".color").forEach((color, index) => {
    currentColorIndex[index] = 0; // Pierwszy kolor z tablicy

    color.addEventListener("click", () => {
        currentColorIndex[index] = (currentColorIndex[index] + 1) % colors.length; // Następny kolor
        color.style.backgroundColor = colors[currentColorIndex[index]]; // Zmiana koloru
    });
});

document.querySelector(".start-button").addEventListener("click", async () => {
    const selectedColors = Array.from(document.querySelectorAll(".color"))
        .map((color, index) => colors[currentColorIndex[index]]);

    try {
        const response = await fetch("/start-game-vs-player", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ colors: selectedColors })
        });

        if (response.ok) {
            const responseBody = await response.text(); // Odczytanie odpowiedzi serwera
            if (responseBody === "OK") {
                // Przekierowanie na adres '/get'
                window.location.href = "/get-game";
            } else {
                console.error("Unexpected response:", responseBody);
            }
        } else {
            console.error("Error starting the game:", response.statusText);
        }
    } catch (error) {
        console.error("Request failed:", error);
    }
});