const colors = ["red", "blue", "green", "yellow", "white", "black"];

function changeColor(element) {
    let currentColor = element.style.backgroundColor;
    let nextColorIndex = (colors.indexOf(currentColor) + 1) % colors.length;
    element.style.backgroundColor = colors[nextColorIndex];
}

// Dodaj obsługę zdarzeń kliknięcia do elementów 'color'
document.querySelectorAll(".game div[id^='g'][id$='c1'], .game div[id^='g'][id$='c2'], .game div[id^='g'][id$='c3'], .game div[id^='g'][id$='c4']").forEach((cell) => {
    cell.addEventListener("click", () => changeColor(cell));
});