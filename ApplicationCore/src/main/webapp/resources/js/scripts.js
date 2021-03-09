function showConfirmEmployment(text, number) {
    let r = confirm(text);
    if (r === true) {
        document.getElementById('EmploymentListForm:j_idt39:' + number + ':hdnBtn').click();
    }
}

function showConfirmBabysitter(text, number) {
    let r = confirm(text);
    if (r === true) {
        document.getElementById('BabysitterListForm:j_idt39:' + number + ':hdnBtn').click();
    }
}

function checkLogin(login, message) {
    let pattern = /^\w{2,}$/
    if (pattern.test(login)) {
        document.getElementById('loginDiv').innerHTML = "";
        return true;
    } else {
        document.getElementById('loginDiv').innerHTML = message;
        return false;
    }
}

function checkName(name, message) {
    let pattern = /^[A-Za-ząćęłńóśżźĄĆĘŁŃÓŚŻŹ]{2,}$/;
    if (pattern.test(name)) {
        document.getElementById('nameDiv').innerHTML = "";
        return true;
    } else {
        document.getElementById('nameDiv').innerHTML = message;
        return false;
    }
}

function checkSurname(surname, message) {
    let pattern = /^[A-Za-ząćęłńóśżźĄĆĘŁŃÓŚŻŹ]{2,}$/;
    if (pattern.test(surname)) {
        document.getElementById('surnameDiv').innerHTML = "";
        return true;
    } else {
        document.getElementById('surnameDiv').innerHTML = message;
        return false;
    }
}

function checkNumber(number, message, outputDiv, min, max) {
    let isNumber = /^\d+$/.test(number);
    let isInRange = false;
    if (isNumber) {
        let parseToInt = parseInt(number);
        isInRange = parseToInt > min && parseToInt < max
    }

    if (isNumber && isInRange) {
        document.getElementById(outputDiv).innerHTML = "";
        return true;
    } else {
        document.getElementById(outputDiv).innerHTML = message;
        return false;
    }
}

function checkAdminSuperUser(msgLogin, msgName, msgSurname) {
    let loginCheck = checkLogin(document.getElementById('j_idt37:login').value, msgLogin);
    let nameCheck = checkName(document.getElementById('j_idt37:name').value, msgName);
    let surnameCheck = checkSurname(document.getElementById('j_idt37:surname').value, msgSurname);
    return (loginCheck && nameCheck && surnameCheck);
}

function checkClient(msgLogin, msgName, msgSurname, msgNumOfChildren, msgAgeOfTheYoungestChild) {
    let loginNameSurnameCheck = checkAdminSuperUser(msgLogin, msgName, msgSurname);
    let numberOfChildrenCheck = checkNumber(document.getElementById('j_idt37:numberOfChildren').value, msgNumOfChildren, 'numberDiv', 0, 15);
    let ageCheck = checkNumber(document.getElementById('j_idt37:ageOfTheYoungestChild').value, msgAgeOfTheYoungestChild, 'ageDiv', 0, 15);
    return (loginNameSurnameCheck && numberOfChildrenCheck && ageCheck);
}

function checkBabysitter(msgName, msgSurname, msgBasePrice, msgMinChildAge, msgMaxNumOfChildren) {
    let nameCheck = checkName(document.getElementById('j_idt37:name').value, msgName);
    let surnameCheck = checkSurname(document.getElementById('j_idt37:surname').value, msgSurname);
    let basePriceCheck = checkNumber(document.getElementById('j_idt37:basePriceForHour').value, msgBasePrice, 'basePriceDiv', 0, 1000);
    let childAgeCheck = checkNumber(document.getElementById('j_idt37:minChildAge').value, msgMinChildAge, 'minChildAgeDiv', 0, 15);
    let numChildrenCheck = checkNumber(document.getElementById('j_idt37:maximalNumberOfChildren').value, msgMaxNumOfChildren, 'maxNumOfChildDiv', 0, 15);
    return (nameCheck && surnameCheck && basePriceCheck && childAgeCheck && numChildrenCheck);
}

function checkTeachingBabysitter(msgName, msgSurname, msgBasePrice, msgMinChildAge, msgMaxNumOfChildren, msgYearsOfExp) {
    let basicPropCheck = checkBabysitter(msgName, msgSurname, msgBasePrice, msgMinChildAge, msgMaxNumOfChildren);
    let yearsOfExpCheck = checkNumber(document.getElementById('j_idt37:yearsOfExperience').value, msgYearsOfExp, 'yearsDiv', 0, 70);
    return (basicPropCheck && yearsOfExpCheck);
}

function checkTidingBabysitter(msgName, msgSurname, msgBasePrice, msgMinChildAge, msgMaxNumOfChildren, msgValueOfEquip) {
    let basicPropCheck = checkBabysitter(msgName, msgSurname, msgBasePrice, msgMinChildAge, msgMaxNumOfChildren);
    let valueOfEquipCheck = checkNumber(document.getElementById('j_idt37:valueOfCleaningEquipment').value, msgValueOfEquip, 'cleaningDiv', 0, 20000);
    return (basicPropCheck && valueOfEquipCheck);
}

//
// function searchBabysitter(searchQuery) {
// }
//
// function buildTable(data, headers) {
//     let table = document.getElementById("BabysitterListForm:babysittersTable");
//     table.innerHTML = '';
//
//     for (let i = 0; i < headers.length; i++) {
//         let row = ``;
//         let rowDict = headers[i];
//         for (let key in rowDict) {
//             row += `\n<th>${rowDict[key]}`
//         }
//         table.innerHTML += row;
//     }
//
//     for (let i = 0; i < data.length; i++) {
//         let row = ``;
//         let rowDict = data[i];
//         for (let key in rowDict) {
//             row += `\n<td>${rowDict[key]}`;
//         }
//         table.innerHTML += row;
//     }
// }
//
// function searchTable(value, data) {
//     let filteredData = [];
//
//     for (let i = 0; i < data.length; i++) {
//         value = value.toLowerCase();
//         let rowDict = data[i];
//         for (let key in rowDict) {
//             if (rowDict[key].toLowerCase().includes(value)) {
//                 filteredData.push(data[i]);
//                 break;
//             }
//         }
//     }
//
//     return filteredData;
// }
//
// function buildFilteredTable(value) {
//     let table = document.getElementById("BabysitterListForm:babysittersTable");
//     let data = [];
//     let rows = table.rows;
//     let headerRows = rows[0].cells;
//     let headers = []
//
//     for (let i = 0; i < headerRows.length; i++) {
//         // let headerName = headerRows[i].innerHTML.replace(/<[\s\S]+/, '').trim(); // to get rid of any left HTML code,
//         // .* does not include newline characters in js
//         let headerName = headerRows[i].innerHTML;
//         headers.push(headerName);
//     }
//
//     for (let i = 1; i < rows.length; i++) {
//         let cells = rows[i].cells;
//         let dict = {}
//         for (let i = 0; i < cells.length; i++) {
//
//             dict[headers[i]] = cells[i].innerText;
//         }
//         data.push(dict);
//     }
//
//     let filteredData = searchTable(value, data)
//     buildTable(filteredData, headers);
// }
//
// const taskList = document.querySelector('.task-list');
//
// const first = document.querySelector('.first');
// const previous = document.querySelector('.previous');
// const next = document.querySelector('.next');
// const last = document.querySelector('.last');
//
// let page = 0;
// let arrayList = [];
// let pageSize = 10;
//
// next.addEventListener('click', () => {
//     page === arrayList.length - pageSize ? page = 0 : page += pageSize;
//     taskList.innerHTML = "";
//     for (let i = page; i < page + pageSize; i++) {
//         taskList.appendChild(arrayList[i])
//     }
// })
//
// previous.addEventListener('click', () => {
//     page === 0 ? page = arrayList.length - pageSize : page -= pageSize;
//     taskList.innerHTML = "";
//     for (let i = page; i < page + pageSize; i++) {
//         taskList.appendChild(arrayList[i]);
//     }
// })
//
// first.addEventListener('click', () => {
//     page = 0;
//     taskList.innerHTML = "";
//     for (let i = 0; i < page + pageSize; i++) {
//         taskList.appendChild(arrayList[i]);
//     }
// });
//
// last.addEventListener('click', () => {
//     page = arrayList.length - pageSize;
//     taskList.innerHTML = "";
//     for (let i = 0; i < page + pageSize; i++) {
//         taskList.appendChild(arrayList[i]);
//     }
// });
//
//
// function genBabysitterTable() {
//     let tables  = document.querySelectorAll('.babysittersTable');
//     for (let i = 0; i < tables.length; i++) {
//         pageSize = parseInt(tables[i].dataset.pagecount);
//         createFooters(tables[i]);
//         createTableMeta(tables[i]);
//         loadTable(tables[i]);
//     }
// }
//
// function loadTable(table) {
//     let startIndex = 0;
//     if (table.querySelector('th')) startIndex = 1;
//
//     let start = (parseInt(table.dataset.currentpage) * table.dataset.pagecount) + startIndex;
//     let end = start + parseInt(table.dataset.pagecount);
//     let rows = table.rows;
//
//     for (let i = startIndex; i < rows.length; i++) {
//         if (i < start || x >= end)
//             rows[i].classList.add('inactive');
//         else
//             rows[i].classList.remove('inactive');
//     }
// }
//
// function createTableMeta(table) {
//     table.dataset.currentpage = "0";
// }
//
// function createFooters(table) {
//     let hasHeader = false;
//     if (table.querySelector('th'))
//         hasHeader = true;
//
//     let rows = table.rows.length;
//
//     if (hasHeader)
//         rows = rows - 1;
//
//     let numPages = rows / perPage;
//     let pager = document.createElement("div");
//
//     // add an extra page, if we're
//     if (numPages % 1 > 0)
//         numPages = Math.floor(numPages) + 1;
//
//     pager.className = "pager";
//     for (let i = 0; i < numPages ; i++) {
//         let page = document.createElement("div");
//         page.innerHTML = i + 1;
//         page.className = "pager-item";
//         page.dataset.index = i;
//
//         if (i === 0)
//             page.classList.add("selected");
//
//         page.addEventListener('click', function() {
//             let parent = this.parentNode;
//             let items = parent.querySelectorAll(".pager-item");
//             for (let x = 0; x < items.length; x++) {
//                 items[x].classList.remove("selected");
//             }
//             this.classList.add('selected');
//             table.dataset.currentpage = this.dataset.index;
//             loadTable(table);
//         });
//         pager.appendChild(page);
//     }
//
//     // insert page at the top of the table
//     table.parentNode.insertBefore(pager, table);
// }
//
// window.addEventListener('load', function() {
//     genBabysitterTable();
// });
//
// function setPageSize(_pageSize) {
//     pageSize = _pageSize;
// }
//
// function filter(filter) {
//     if (filter == undefined) filter = document.getElementById()
// }

window.onload = function () {
    let selection = document.getElementById('BabysitterListForm:babysittersTable:babysitterIDSelect');
    if (selection) {
        selection.addEventListener('change', function () {
            let selectionVal = selection.value;
            console.log(selectionVal);
            if (selectionVal === 0) {
                $("#BabysitterListForm\\:babysittersTable tr").show();
            } else {
                $("#BabysitterListForm\\:babysittersTable tr").each(function () {
                    if ($(this).find("th").length === 0) {
                        if ($(this).find("td").eq(0).text().trim() === selectionVal)
                            $(this).show();
                        else $(this).hide();
                    }
                });
            }
        });
    }
}
// function test() {
//
//
//
// }

// BabysitterListForm:babysittersTable_head

// if (/^\D?$/.test(selection)) {
//     $()
// }