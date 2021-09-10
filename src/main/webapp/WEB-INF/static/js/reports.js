var reportLang = {
    info: 'Страница _PAGE_ из _PAGES_',
    lengthMenu: 'На странице _MENU_',
    zeroRecords: 'Проб не найдено',
    infoEmpty: 'Не найдено',
    search: "Поиск",
    processing: "Загрузка...",
    loadingRecords: "Загрузка...",
    paginate: {
        first: "|<",
        last: ">|",
        next: ">",
        previous: "<"
    }
};

var editButton = {
    title: "",
    data: null,
    width: "4px",
    className: "dt-center editor-edit align-middle",
    render: function (data, type, row, meta) {
        return '<a href="/webService/private/reports/' + data.id + '" class="text-info bi bi-pencil h4"></a>'
    },
    orderable: false
};

var deleteButton = {
    title: "",
    data: null,
    width: "4px",
    className: "dt-center editor-delete align-middle",
    render: function (data, type, row, meta) {
        return '<a href="/webService/private/reports/' + data.id + '/delete" onclick="return DeleteFunction(\'Удалить документ?\');" class="text-danger bi bi-trash h4"></a>'
    },
    orderable: false
};

$(document)
    .ready(
        function () {
            console.log('report js load');
            var selected = [];

            $('.nav-tabs a').on('shown.bs.tab', function (e) {
                var tab = e.target.hash.substring(1);
                window.history.pushState("", "", "?tab=" + tab);
            });

            $('.report-table, .report-select-table')
                .DataTable(
                    {
                        ajax: {
                            url: '/webService/private/reports/dataTable'
                        },
                        rowId: 'id',
                        pagingType: 'first_last_numbers',
                        language: reportLang,
                        rowCallback: function (row, data) {
                            if ($.inArray(row.id, selected) !== -1) {
                                $(row).addClass('selected');
                            }
                        },
                        columns: [
                            {
                                title: 'Код',
                                data: 'id',
                                width: '5rem',
                                className: 'h5 text-body-right text-center'
                            },
                            {
                                title: 'Наименование',
                                data: 'name',
                                className: 'h5'
                            },
                            {
                                title: 'Файл',
                                data: 'fileName',
                                className: 'h5',
                                width: '20rem'
                            },
                            {
                                title: "",
                                data: null,
                                width: "4px",
                                className: "dt-center align-middle",
                                render: function (data, type, row, meta) {
                                    return '<a target="_blank" href="/webService/private/reports/' + data.id + '/show" class="text-danger bi bi-eye h4"></a>'
                                },
                                orderable: false
                            },
                            {
                                title: 'ИСХ',
                                data: null,
                                className: 'h5',
                                width: '5rem',
                                render: function (data, type, row) {
                                    return '<span class="bi ' + (data.existsSource ? 'bi-check-circle text-success' : 'bi-x-circle text-danger') + ' h4"></span>' +
                                        (data.existsSource ? '<a href="/webService/private/reports/' + data.id + '/source" class="text-info bi bi-cloud-download h4 ml-3"></a>' : '')
                                }
                            },
                            {
                                title: 'КОМП',
                                data: null,
                                className: 'h5',
                                width: '5rem',
                                render: function (data, type, row) {
                                    return '<span class="bi ' + (data.existsSource ? 'bi-check-circle text-success' : 'bi-x-circle text-danger') + ' h4"></span>'
                                }
                            },
                            editButton,
                            deleteButton
                        ],
                        buttons: [
                            {
                                text: '&nbsp;Создать',
                                className: 'btn btn-info btn-sm bi bi-person-plus',
                                action: function (e, dt, node, config) {
                                    window.location.href = '/webService/private/reports/new';
                                }
                            }
                        ]
                    });

            $('.custom-file-input').on('change', function (e) {
                e.target.nextElementSibling.innerText = this.files[0].name;
                const data = new FormData();
                let file = e.target.files[0];
                data.append('data-source', file);

                $.post({
                    url: '/webService/private/reports/upload-source',
                    data: data,
                    contentType: false,
                    processData: false,
                    cache: false,
                    dataType: 'json'
                }).done(function (data) {
                    const alert = $(e.target).parent().parent().siblings('.alert')[0];
                    alert.innerText = data.message;
                    $(alert).show().delay(5000).fadeOut();
                    $('#report-properties').load('/webService/private/reports/report-properties');
                })
            });
        });