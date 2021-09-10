var sampleLang = {
    info: 'Страница _PAGE_ из _PAGES_',
    lengthMenu: 'На странице _MENU_',
    zeroRecords: 'Актов отбора/приема не найдено',
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
        return '<a href="/webService/private/samplings/' + data.id + '" class="text-info bi bi-pencil h4"></a>'
    },
    orderable: false
};

var deleteButton = {
    title: "",
    data: null,
    width: "4px",
    className: "dt-center editor-delete align-middle",
    render: function (data, type, row, meta) {
        return '<a href="/webService/private/samplings/' + data.id + '/delete" onclick="return DeleteFunction(\'Удалить документ?\');" class="text-danger bi bi-trash h4"></a>'
    },
    orderable: false
};

$(document)
    .ready(
        function () {
            console.log('sample js load');
            var selected = [];

            $('.nav-tabs a').on('shown.bs.tab', function (e) {
                var tab = e.target.hash.substring(1);
                window.history.pushState("", "", "?tab=" + tab);
            });

            $('.sample-table, .sample-select-table')
                .DataTable(
                    {
                        ajax: {
                            url: '/webService/private/samplings/dataTable'
                        },
                        rowId: 'id',
                        pagingType: 'first_last_numbers',
                        language: sampleLang,
                        rowCallback: function (row, data) {
                            if ($.inArray(row.id, selected) !== -1) {
                                $(row).addClass('selected');
                            }
                        },
                        columns: [
                            {
                                title: 'Номер',
                                data: 'number',
                                width: '5rem',
                                className: 'h5 text-body-right text-center'
                            },
                            {
                                title: 'Дата создания',
                                data: 'createDate',
                                // width: '12rem',
                                className: 'h5 text-center'
                            },
                            editButton,
                            deleteButton
                        ],
                        buttons: [
                            {
                                text: '&nbsp;Создать',
                                className: 'btn btn-info btn-sm bi bi-person-plus',
                                action: function (e, dt, node, config) {
                                    window.location.href = '/webService/private/samples/new';
                                }
                            }
                        ]
                    });
        });