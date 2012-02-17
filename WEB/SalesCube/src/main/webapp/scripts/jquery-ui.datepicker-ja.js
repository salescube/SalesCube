jQuery(function($){
    $.datepicker.regional['ja'] = {clearText: '', clearStatus: '',
        closeText: '閉じる', closeStatus: '',
        prevText: '<前月', prevStatus: '',
        nextText: '次月>', nextStatus: '',
        currentText: '今日', currentStatus: '',
        monthNames: ['1月','2月','3月','4月','5月','6月',
        '7月','8月','9月','10月','11月','12月'],
        monthNamesShort: ['1月','2月','3月','4月','5月','6月',
        '7月','8月','9月','10月','11月','12月'],
        monthStatus: '', yearStatus: '',
        weekHeader: 'Wk', weekStatus: '',
        dayNames: ['日','月','火','水','木','金','土'],
        dayNamesShort: ['日','月','火','水','木','金','土'],
        dayNamesMin: ['日','月','火','水','木','金','土'],
        dayStatus: 'DD', dateStatus: 'D, M d',
        dateFormat: 'yy/mm/dd', firstDay: 0,
        initStatus: '', isRTL: false};
    $.datepicker.setDefaults($.datepicker.regional['ja']);
});
