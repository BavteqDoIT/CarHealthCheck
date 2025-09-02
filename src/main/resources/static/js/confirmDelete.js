function confirmDelete(carId, message) {
    if (confirm(message)) {
        const csrfToken = document.querySelector('input[name="_csrf"]').value;

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/design/summary/delete/' + carId;

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = '_csrf';
        input.value = csrfToken;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();
    }
}