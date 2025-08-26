let damageIndex = /*[[${paintCheck.damages.size()}]]*/ 0;

function addDamage() {
    const container = document.getElementById("damages-container");

    const html = `
    <div class="border rounded p-3 mb-3 damage-item">
        <div class="row g-2 align-items-end">
            <div class="col-md-3">
                <label class="form-label">Body part</label>
                <select class="form-select" name="damages[${damageIndex}].bodyPart">
                    <option value="Hood">Hood</option>
                    <option value="Roof">Roof</option>
                    <option value="Door">Door</option>
                    <option value="Trunk">Trunk</option>
                    <option value="Pillar">Pillar</option>
                    <option value="Wheel_Arch">Wheel Arch</option>
                    <option value="Other">Other</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label">Damage type</label>
                <select class="form-select" name="damages[${damageIndex}].damageType">
                    <option value="Scratch">Scratch</option>
                    <option value="Dent">Dent</option>
                    <option value="Chip">Chip</option>
                    <option value="Scuff">Scuff</option>
                </select>
            </div>
            <div class="col-md-3">
                <label class="form-label">Size</label>
                <select class="form-select" name="damages[${damageIndex}].size">
                    <option value="Small">Small</option>
                    <option value="Medium">Medium</option>
                    <option value="Large">Large</option>
                </select>
            </div>
            <div class="col-md-3 text-end">
                <button type="button" class="btn btn-outline-danger remove-damage">Remove</button>
            </div>
        </div>
    </div>
    `;
    container.insertAdjacentHTML("beforeend", html);
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