function confirmDelete(carId) {
    if (confirm('Czy na pewno chcesz usunąć?')) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/design/summary/delete/' + carId;
        document.body.appendChild(form);
        form.submit();
    }
}