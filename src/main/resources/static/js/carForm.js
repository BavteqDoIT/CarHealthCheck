$(document).ready(function () {
    const brandSelect = $('#brandSelect');
    const modelSelect = $('#modelSelect');
    const engineSelect = $('#engineSelect');

    brandSelect.change(function () {
        const brandId = $(this).val();

        modelSelect.empty().append(`<option value="">${msgSelectModel}</option>`).prop('disabled', true);
        engineSelect.empty().append(`<option value="">${msgSelectModelFirst}</option>`).prop('disabled', true);

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

        engineSelect.empty();

        if (modelId) {
            engineSelect.append(`<option value="">${msgSelectEngineType}</option>`);

            $.get('/api/engine/' + modelId, function (data) {
                $.each(data, function (i, engine) {
                    engineSelect.append(`<option value="${engine.id}">${engine.displayName}</option>`);
                });
                engineSelect.prop('disabled', false);
            });
        } else {
            engineSelect.append(`<option value="">${msgSelectModelFirst}</option>`).prop('disabled', true);
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
