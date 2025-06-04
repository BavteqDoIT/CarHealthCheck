$(document).ready(function () {
    const brandSelect = $('#brandSelect');
    const modelSelect = $('#modelSelect');
    const engineSelect = $('#engineSelect');

    brandSelect.change(function () {
        const brandId = $(this).val();

        modelSelect.empty().append('<option value="">Select a model</option>').prop('disabled', true);
        engineSelect.empty().append('<option value="">Select a model first</option>').prop('disabled', true);

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
            engineSelect.append('<option value="">Select an engine type</option>');

            $.get('/api/engine/' + modelId, function (data) {
                $.each(data, function (i, engine) {
                    engineSelect.append(`<option value="${engine.id}">${engine.engineType}</option>`);
                });
                engineSelect.prop('disabled', false);
            });
        } else {
            engineSelect.append('<option value="">Select a model first</option>').prop('disabled', true);
        }
    });
});
