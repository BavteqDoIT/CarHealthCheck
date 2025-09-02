function confirmDelete(carId) {
    if (confirm('Czy na pewno chcesz usunąć?')) {
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
