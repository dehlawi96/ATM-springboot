// Simple frontend that calls the Spring Boot endpoints
const insertBtn = document.getElementById('insertBtn');
const ui = document.getElementById('ui');
const pinArea = document.getElementById('pinArea');
const pinInput = document.getElementById('pinInput');
const pinSubmit = document.getElementById('pinSubmit');
const menuArea = document.getElementById('menuArea');
const screenText = document.getElementById('screen-text');
const actionArea = document.getElementById('actionArea');
const actionContent = document.getElementById('actionContent');
const actionBack = document.getElementById('actionBack');

const btnBalance = document.getElementById('btnBalance');
const btnDeposit = document.getElementById('btnDeposit');
const btnWithdraw = document.getElementById('btnWithdraw');
const btnHistory = document.getElementById('btnHistory');
const btnLogout = document.getElementById('btnLogout');

let token = null;
let seededCardNumber = "12345678"; // seeded in backend

insertBtn.addEventListener('click', () => {
    screenText.innerText = "Card inserted. Please enter PIN.";
    ui.classList.remove('hidden');
    pinArea.classList.remove('hidden');
    menuArea.classList.add('hidden');
    actionArea.classList.add('hidden');
    pinInput.value = '';
    pinInput.focus();
});

pinSubmit.addEventListener('click', async () => {
    const pin = pinInput.value.trim();
    if (!pin) { screenText.innerText = "Enter PIN"; return; }
    try {
        const res = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({cardNumber: seededCardNumber, pin: Number(pin)})
        });
        if (!res.ok) {
            const err = await res.json();
            screenText.innerText = err.error || 'Auth failed';
            return;
        }
        const data = await res.json();
        token = data.token;
        screenText.innerText = "Authenticated";
        pinArea.classList.add('hidden');
        menuArea.classList.remove('hidden');
    } catch (e) {
        screenText.innerText = 'Network error';
        console.error(e);
    }
});

btnBalance.addEventListener('click', async () => {
    try {
        const res = await fetch('/api/account/balance', { headers: { Authorization: 'Bearer ' + token }});
        if (!res.ok) {
            screenText.innerText = 'Auth required';
            return;
        }
        const data = await res.json();
        screenText.innerText = 'Balance: ₹' + data.balance;
    } catch(e) {
        screenText.innerText = 'Network error';
    }
});

btnDeposit.addEventListener('click', () => {
    showAction(`<h4>Deposit</h4>
    <input id="depositAmt" type="number" placeholder="Amount" />
    <button id="doDeposit">Submit</button>`);
    document.getElementById('doDeposit').addEventListener('click', async () => {
        const amt = Number(document.getElementById('depositAmt').value);
        if (!amt || amt <= 0) { actionContent.innerText = 'Invalid amount'; return; }
        const res = await fetch('/api/account/deposit', {
            method:'POST',
            headers: {'Content-Type':'application/json', Authorization: 'Bearer ' + token},
            body: JSON.stringify({amount: amt})
        });
        if (!res.ok) {
            const e = await res.json();
            actionContent.innerText = e.error || 'Failed';
            return;
        }
        const data = await res.json();
        screenText.innerText = 'Balance: ₹' + data.balance;
        hideActionAfter('Deposited: ₹' + amt);
    });
});

btnWithdraw.addEventListener('click', () => {
    showAction(`<h4>Withdraw</h4>
    <input id="withdrawAmt" type="number" placeholder="Amount" />
    <button id="doWithdraw">Submit</button>`);
    document.getElementById('doWithdraw').addEventListener('click', async () => {
        const amt = Number(document.getElementById('withdrawAmt').value);
        if (!amt || amt <= 0) { actionContent.innerText = 'Invalid amount'; return; }
        const res = await fetch('/api/account/withdraw', {
            method:'POST',
            headers: {'Content-Type':'application/json', Authorization: 'Bearer ' + token},
            body: JSON.stringify({amount: amt})
        });
        if (!res.ok) {
            const e = await res.json();
            actionContent.innerText = e.error || 'Failed';
            return;
        }
        const data = await res.json();
        screenText.innerText = 'Balance: ₹' + data.balance;
        hideActionAfter('Withdrawn: ₹' + amt);
    });
});

btnHistory.addEventListener('click', async () => {
    const res = await fetch('/api/account/transactions', { headers: { Authorization: 'Bearer ' + token }});
    if (!res.ok) { screenText.innerText = 'Auth required'; return; }
    const arr = await res.json();
    if (!Array.isArray(arr) || arr.length === 0) {
        actionContent.innerText = 'No transactions yet.';
    } else {
        actionContent.innerHTML = '<h4>Transactions</h4>' + arr.map(t => `<div>${t.dateTime} | ${t.type} | ₹${t.amount}</div>`).join('');
    }
    showActionArea();
});

btnLogout.addEventListener('click', async () => {
    await fetch('/api/auth/logout', { method: 'POST', headers: { Authorization: 'Bearer ' + token }});
    token = null;
    screenText.innerText = 'Card ejected. Goodbye!';
    ui.classList.add('hidden');
});

actionBack.addEventListener('click', () => {
    actionArea.classList.add('hidden');
    menuArea.classList.remove('hidden');
});

function showAction(html) {
    actionContent.innerHTML = html;
    actionArea.classList.remove('hidden');
    menuArea.classList.add('hidden');
}

function showActionArea() {
    actionArea.classList.remove('hidden');
    menuArea.classList.add('hidden');
}

function hideActionAfter(msg) {
    actionContent.innerText = msg;
    setTimeout(() => {
        actionArea.classList.add('hidden');
        menuArea.classList.remove('hidden');
    }, 1000);
}
