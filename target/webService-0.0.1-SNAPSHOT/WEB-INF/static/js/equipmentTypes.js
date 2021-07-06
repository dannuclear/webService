var docTypeLang = {
    info: 'Страница _PAGE_ из _PAGES_',
    lengthMenu: 'На странице _MENU_',
    zeroRecords: 'Видов документов не найдено',
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

$(document)
    .ready(
        function () {
            console.log('equipment types js load');
            var selected = [];

            var documentTypeSelect2 = $('.equipment-type-select2').select2({
                theme: 'bootstrap4',
                containerCssClass: 'custom-class',
                // dropdownCssClass: ':all:',
                ajax: {
                    url: function (params) {
                        return "/webService/api/v1/equipmentTypes" + (params.term === undefined ? '' : '?search=' + params.term);
                    },
                    dataType: 'json',
                    cache: false,
                    delay: 250,
                    processResults: function (data) {
                        if (data._embedded == undefined)
                            return {results: []};
                        let result = $.map(data._embedded.equipmentTypes, function (item, index) {
                            return {
                                id: item.id,
                                text: item.name
                            };
                        })
                        return {
                            results: result
                        };
                    }
                }
            });

            $('.equipment-type-table, .equipment-type-select-table')
                .DataTable(
                    {
                        ajax: {
                            url: '/webService/private/equipmentTypes/dataTable'
                        },
                        rowId: 'id',
                        pagingType: 'first_last_numbers',
                        language: docTypeLang,
                        rowCallback: function (row, data) {
                            if ($.inArray(row.id, selected) !== -1) {
                                $(row).addClass('selected');
                            }
                        },
                        columns: [
                            {
                                title: 'id',
                                data: 'id'
                            },
                            {
                                title: 'Наименование',
                                data: 'name'
                            },
                            {
                                title: 'Аббр.',
                                data: 'shortName'
                            },
                            {
                                title: "",
                                data: null,
                                width: "4px",
                                className: "dt-center editor-edit align-middle",
                                render: function (data, type, row, meta) {
                                    return '<a href="/webService/private/documentTypes/' + data.id + '" class="text-info bi bi-pencil h4"></a>'
                                },
                                orderable: false
                            },
                            {
                                title: "",
                                data: null,
                                width: "4px",
                                className: "dt-center editor-delete align-middle",
                                render: function (data, type, row, meta) {
                                    return '<a href="/webService/private/documentTypess/' + data.id + '/delete" onclick="return DeleteFunction(\'Удалить документ?\');" class="text-danger bi bi-trash h4"></a>'
                                },
                                orderable: false
                            }
                        ],
                        buttons: [
                            {
                                text: '&nbsp;Создать',
                                className: 'btn btn-info btn-sm bi bi-person-plus',
                                action: function ( e, dt, node, config ) {
                                    dt.ajax.reload();
                                }
                            }
                        ],
                        dom: '<"row"<"toolbar col-sm-12 col-md-6"B><"col-sm-12 col-md-6"fb>>'
                            + '<"row"<"col-sm-12"tr>>'
                            + '<"row"<"col-sm-12 col-md-3"i><"col-sm-12 col-md-6"p><"col-sm-4 col-md-3"l>>'
                    });

            $('.equipment-type-select-table tbody').on('click', 'tr', function () {
                const id = this.id;
                const index = $.inArray(id, selected);
                if (index === -1) {
                    selected.push(id);
                } else {
                    selected.splice(index, 1);
                }
                $(this).toggleClass('selected');
            });

            $('.modal').on('shown.bs.modal', function () {
                $('.equipment-type-select-table').DataTable().columns.adjust();
            })

            // $('div.toolbar').html('<a class="btn btn-outline-dark btn-sm bi bi-person-plus" href="/webService/private/documentTypes/new">&nbsp;Создать</a>');

            $('#equipment-type-table-dialog').find('#submit-button').on('click', function (e) {
                if (selected.length == 0)
                    return;
                $.ajax({
                    url: "/webService/api/v1/equipmentTypes/" + selected.join()
                }).done(function (data, textStatus, jqXHR) {
                    data = Array.isArray(data) ? data : [data];
                    const options = data.map(function (item) {
                        return new Option(item.name, item.id, false, true);
                    });
                    documentTypeSelect2.each(function (e) {
                        $(this).empty();
                        options.forEach(op => $(this).append(op));
                        $(this).trigger('change');
                    });
                });
            })

        })
;