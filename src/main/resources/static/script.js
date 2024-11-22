const colors = ["white", "blue", "green", "yellow", "red", "black"];
let currentRound = 1; // Zaczynamy od pierwszej rundy
let checkButton = document.getElementById("checkButton");

console.log("current round", currentRound);

// Funkcja zmieniająca kolor na następny w tablicy
function changeColor(element) {
    // Sprawdzamy obecny kolor
    let currentColor = element.style.backgroundColor;

    // Jeśli element nie ma ustawionego koloru, ustawiamy pierwszy kolor z tablicy
    if (!currentColor) {
        element.style.backgroundColor = colors[0];
    } else {
        // Inaczej zmieniamy kolor na kolejny z tablicy
        let nextColorIndex = (colors.indexOf(currentColor) + 1) % colors.length;
        element.style.backgroundColor = colors[nextColorIndex];
    }
}

// Dodaj obsługę zdarzeń kliknięcia do elementów 'color'
document.querySelectorAll(".game div[id^='g'][id$='c1'], .game div[id^='g'][id$='c2'], .game div[id^='g'][id$='c3'], .game div[id^='g'][id$='c4']").forEach((cell) => {
    cell.addEventListener("click", () => changeColor(cell));
});

// Ustaw aktywny rząd
function setActiveRow() {
    const rows = document.querySelectorAll(".guess-row");
    rows.forEach(row => row.classList.remove("active-row"));

    const activeRow = document.querySelector(`#g${currentRound}`);
    if (activeRow) {
        activeRow.classList.add("active-row");
    }
}

// Funkcja do konwertowania koloru RGB na nazwę koloru
function rgbToColorName(rgb) {
    const colorMap = {
        "rgb(255, 0, 0)": "red",
        "rgb(0, 128, 0)": "green",
        "rgb(0, 0, 255)": "blue",
        "rgb(255, 255, 0)": "yellow",
        "rgb(255, 255, 255)": "white",
        "rgb(0, 0, 0)": "black"
    };

    return colorMap[rgb] || rgb; // Jeśli nie ma w mapie, zwróci RGB
}

// Funkcja do zebrania kolorów z aktywnego rzędu
function getColorsFromActiveRow() {
    const activeRow = document.querySelector(`#g${currentRound}`);
    if (!activeRow) return [];

    // Wyszukaj wszystkie divy z kolorami, których ID zaczyna się od g{round}-c
    const colorDivs = activeRow.querySelectorAll(`div[id^='g${currentRound}-c']`);
    console.log("colorsDivs", colorDivs.length);

    // Zbierz kolory i przekształć je na nazwy
    return Array.from(colorDivs).map(div => {
        const style = window.getComputedStyle(div);
        const backgroundColor = style.backgroundColor;
        console.log(`Element ${div.id} has background color: ${backgroundColor}`);
        return rgbToColorName(backgroundColor); // Zmieniamy RGB na nazwę koloru
    });
}

// Funkcja do wyświetlania popupu z wynikiem
function showGameResult(status) {
    if (status === 'PLAYER_WON') {
        checkButton.classList.add("hidden");
        disableAllColors();
        alert("Congratulations, you won!");
    } else if (status === 'PLAYER_LOST') {
        checkButton.classList.add("hidden");
        disableAllColors();
        alert("Unfortunately, you lost.");
    }
}

// Obsługa kliknięcia przycisku Check
document.getElementById("checkButton").addEventListener("click", async () => {
    const colors = getColorsFromActiveRow();  // Nie przekazujemy już round
    console.log("Sending guess:", JSON.stringify(colors));

    try {
        const response = await fetch("/submitGuess", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({colors: colors})
        });

        if (response.ok) {
            const result = await response.json();
            console.log("Server response:", result);

            updateHits(currentRound, result.blackHits, result.whiteHits);
            console.log("blackhits" + result.blackHits);
            console.log("whitehits" + result.whiteHits);
            // Wyświetlanie komunikatu w zależności od statusu gry
            showGameResult(result.gameStatus);
        } else {
            console.error("Failed to submit guess:", response.statusText);
        }
    } catch (error) {
        console.error("Error during submission:", error);
    }

    // Przejdź do następnej rundy
    currentRound++;
    if (currentRound <= 10) {
        setActiveRow();  // Używamy currentRound, nie trzeba przekazywać go jako argument
    }
});

function disableAllColors() {
    // Znajdź wszystkie divy z klasą 'guess-row'
    const rows = document.querySelectorAll(".guess-row");

    rows.forEach(row => {
        // Znajdź wszystkie komórki w danym rzędzie, które zawierają kolor
        const colorDivs = row.querySelectorAll("div[id^='g'][id*='-c']");

        colorDivs.forEach(div => {
            // Dodaj klasę 'disabled', aby zablokować interakcje
            div.classList.add("disabled");
            div.style.pointerEvents = "none";  // Blokuje interakcje (np. kliknięcia)
        });
    });
}


function updateHits(rowId, blackHits, whiteHits) {
    const hitRow = document.getElementById(`hit${rowId}`); // Pobiera odpowiedni wiersz np. hit3
    if (!hitRow) {
        console.error(`No row found with id: hit${rowId}`);
        return;
    }

    // Pobiera wszystkie elementy h1, h2, h3, h4 w odpowiednim rzędzie
    const hitDivs = hitRow.querySelectorAll(".h1, .h2, .h3, .h4");
    if (hitDivs.length === 0) {
        console.error(`No hit elements found in row: hit${rowId}`);
        return;
    }

    let index = 0;

    // Ustaw czarne tło dla czarnych trafień
    for (let i = 0; i < blackHits; i++) {
        if (index < hitDivs.length) {
            hitDivs[index].style.setProperty("background-color", "black", "important");
            index++;
        }
    }

    // Ustaw białe tło dla białych trafień
    for (let i = 0; i < whiteHits; i++) {
        if (index < hitDivs.length) {
            hitDivs[index].style.setProperty("background-color", "white", "important");
            index++;
        }
    }

    // Upewnij się, że pozostałe pola są puste
    for (; index < hitDivs.length; index++) {
        hitDivs[index].style.backgroundColor = "transparent";
    }
}


// Ustaw aktywny rząd na start
setActiveRow();
