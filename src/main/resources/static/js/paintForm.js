let damageIndex = /*[[${paintCheck.damages.size()}]]*/ 0;

function addDamage() {
    const template = document.getElementById("damage-template").innerHTML;
    const html = template.replace(/__index__/g, damageIndex); // zamiana placeholdera na numer
    document.getElementById("damages-container").insertAdjacentHTML("beforeend", html);
    attachRemoveHandlers();
    damageIndex++;
}

function attachRemoveHandlers() {
    document.querySelectorAll(".remove-damage").forEach(btn => {
        btn.onclick = function() {
            this.closest(".damage-item").remove();
            updateIndexes();
        };
    });
}

function updateIndexes() {
    const items = document.querySelectorAll("#damages-container .damage-item");
    items.forEach((item, index) => {
        item.querySelectorAll("select").forEach(select => {
            const name = select.getAttribute("name");
            const field = name.split(".")[1];
            select.setAttribute("name", `damages[${index}].${field}`);
        });
    });
    damageIndex = items.length;
}

attachRemoveHandlers();

document.addEventListener('DOMContentLoaded', function () {
    const thicknessCheckbox = document.getElementById('noThicknessMeasurements');

    function toggleThicknessInputs() {
        const thicknessInputs = document.querySelectorAll('input[type="number"]');
        thicknessInputs.forEach(input => {
            if (thicknessCheckbox.checked) {
                input.value = "";
                input.disabled = true;
            } else {
                input.disabled = false;
            }
        });
    }

    toggleThicknessInputs();

    thicknessCheckbox.addEventListener('change', toggleThicknessInputs);
});