$(document).ready(function () {
    const brandSelect = $('#brandSelect');
    const modelSelect = $('#modelSelect');
    const fuelSelect  = $('#fuelSelect');
    const engineSelect = $('#engineSelect');

    function resetModel() {
        modelSelect.empty().append(`<option value="">${msgSelectModel}</option>`).prop('disabled', true);
        resetFuel();
        resetEngine();
    }

    function resetFuel() {
        fuelSelect.empty().append(`<option value="">${msgSelectModelFirst}</option>`).prop('disabled', true);
    }

    function resetEngine() {
        engineSelect.empty().append(`<option value="">${msgSelectFuelFirst}</option>`).prop('disabled', true);
    }

    brandSelect.change(function () {
        const brandId = $(this).val();
        resetModel();

        if (brandId) {
            $.get('/api/models/' + brandId, function (data) {
                $.each(data, function (i, model) {
                    modelSelect.append(`<option value="${model.id}">${model.modelName}</option>`);
                });
                modelSelect.prop('disabled', false);
            });
        }
    });

    modelSelect.change(function () {
        const modelId = $(this).val();
        resetFuel();
        resetEngine();

        if (modelId) {
            fuelSelect.empty().append(`<option value="">${msgSelectFuel}</option>`);

            $.get('/api/fuels/' + modelId, function (data) {
                $.each(data, function (i, fuel) {
                    fuelSelect.append(`<option value="${fuel.id}">${fuel.displayName}</option>`);
                });
                fuelSelect.prop('disabled', false);
            });
        }
    });

    fuelSelect.change(function () {
        const modelId = modelSelect.val();
        const fuelId = $(this).val();

        engineSelect.empty();

        if (modelId && fuelId) {
            engineSelect.append(`<option value="">${msgSelectEngineType}</option>`);

            $.get('/api/engine/' + modelId + '?fuelId=' + fuelId, function (data) {
                $.each(data, function (i, engine) {
                    engineSelect.append(`<option value="${engine.id}">${engine.displayName}</option>`);
                });
                engineSelect.prop('disabled', false);
            });
        } else {
            resetEngine(false);
        }
    });

    const checkbox = $('#foreignRegistered');
    const dateInput = $('#firstRegistrationDate');

    checkbox.change(function () {
        if ($(this).is(':checked')) {
            dateInput.val('').prop('readonly', true);
        } else {
            dateInput.val('').prop('readonly', false);
        }
    });
});
