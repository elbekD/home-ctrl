<table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp">
    <thead>
    <tr>
        <th class="mdl-data-table__cell--non-numeric" colspan="2">${card.date?string.@mDate}</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td class="mdl-data-table__cell--non-numeric">To</td>
        <td>${card.to}</td>
    </tr>
    <tr>
        <td class="mdl-data-table__cell--non-numeric">From</td>
        <td>${card.from}</td>
    </tr>
    <tr>
        <td class="mdl-data-table__cell--non-numeric">Quantity</td>
        <td>${card.to - card.from}</td>
    </tr>
    <tr>
        <td class="mdl-data-table__cell--non-numeric">Amount</td>
        <td>${card.amount}</td>
    </tr>
    <tr>
        <td class="mdl-data-table__cell--non-numeric">Per unit</td>
        <td>${card.amount / (card.to - card.from)}</td>
    </tr>
    </tbody>
</table>