var docLang = {
    info: 'Страница _PAGE_ из _PAGES_',
    lengthMenu: 'На странице _MENU_',
    zeroRecords: 'Документов не найдено',
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

function documentStatus(data) {
    switch (data) {
        case 'ACTIVE':
            return '<div class="bg-success border rounded-sm text-center">Активный</div>'
        case 'REJECT':
            return '<div class="bg-danger border rounded-sm text-center">Прекращен</div>'
    }
    return data;
}

$(document)
    .ready(
        function () {
            console.log('documents js load');
            var selected = [];

            $('.document-select2').select2({
                theme: 'bootstrap4',
                ajax: {
                    url: function (params) {
                        return "/webService/api/v1/documents" + (params.term === undefined ? '' : '?search=' + params.term);
                    },
                    dataType: 'json',
                    cache: false,
                    delay: 250,
                    processResults: function (data) {
                        let result = $.map(data._embedded.documentList, function (item, index) {
                            return {
                                id: item.id,
                                text: item.title
                            };
                        })
                        return {
                            results: result
                        };
                    }
                }
            });

            $('.document-table, .document-select-table')
                .DataTable(
                    {
                        ajax: {
                            url: '/webService/private/documents/dataTable'
                        },
                        rowId: 'id',
                        pagingType: 'first_last_numbers',
                        language: docLang,
                        rowCallback: function (row, data) {
                            if ($.inArray(row.id, selected) !== -1) {
                                $(row).addClass('selected');
                            }
                        },
                        columns: [
                            {
                                title: 'Заголовок',
                                data: 'header'
                            },
                            {
                                title: 'Принят',
                                data: 'commitDate',
                                width: '5rem',
                                className: 'text-center'
                            },
                            {
                                title: 'Комментрарий',
                                data: 'comment',
                                visible: false
                            },
                            {
                                title: 'Статус',
                                data: 'status',
                                width: '5rem',
                                className: 'text-center align-middle',
                                render: function (data, type, row, meta) {
                                    return documentStatus(data);
                                }
                            },
                            {
                                title: "",
                                data: null,
                                width: "4px",
                                className: "dt-center editor-edit align-middle",
                                render: function (data, type, row, meta) {
                                    return '<a href="/webService/private/documents/' + data.id + '" class="text-info bi bi-pencil h4"></a>'
                                },
                                orderable: false
                            },
                            {
                                title: "",
                                data: null,
                                width: "4px",
                                className: "dt-center editor-delete align-middle",
                                render: function (data, type, row, meta) {
                                    return '<a href="/webService/private/documents/' + data.id + '/delete" onclick="return DeleteFunction(\'Удалить документ?\');" class="text-danger bi bi-trash h4"></a>'
                                },
                                orderable: false
                            }
                        ],
                        dom: '<"row"<"toolbar col-sm-12 col-md-6"><"col-sm-12 col-md-6"fb>>'
                            + '<"row"<"col-sm-12"tr>>'
                            + '<"row"<"col-sm-12 col-md-3"i><"col-sm-12 col-md-6"p><"col-sm-4 col-md-3"l>>'
                    });

            $('.document-select-table tbody').on('click', 'tr', function () {
                var id = this.id;
                var index = $.inArray(id, selected);

                if (index === -1) {
                    selected.push(id);
                } else {
                    selected.splice(index, 1);
                }

                $(this).toggleClass('selected');
            });

            $('.modal').on('shown.bs.modal', function () {
                $('.document-select-table').DataTable().columns.adjust();
            })

            $('div.toolbar').html('<a class="btn btn-outline-dark btn-sm bi bi-person-plus" href="/webService/private/documents/new">&nbsp;Создать</a>');
        });