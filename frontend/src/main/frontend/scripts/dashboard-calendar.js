import monthCalendarStyles from "../styles/month-calendar.css?inline";
import monthCalendarGridStyles from "../styles/month-calendar-grid.css?inline";

const calendarLocalization = {
  monthNames: [
    "январь", "февраль", "март", "апрель", "май", "июнь",
    "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"
  ],
  weekdays: [
    "воскресенье", "понедельник", "вторник", "среда",
    "четверг", "пятница", "суббота"
  ],
  weekdaysShort: ["Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб"],
  firstDayOfWeek: 1,
  today: "Сегодня",
  cancel: "Отмена"
};

const calendarStates = new WeakMap();
const statusColors = {
  new: "#4a6fa5",
  in_progress: "#4a8f5b",
  first_control: "#b8952e",
  second_control: "#a94a4a",
  deadline: "#8b5cf6",
  completed: "#7a7a7a"
};

function addStyles(root, id, css) {
  if (root.querySelector(`style[data-dashboard-calendar-style="${id}"]`)) return;
  const style = document.createElement("style");
  style.dataset.dashboardCalendarStyle = id;
  style.textContent = css;
  root.append(style);
}

function renderOrderMarkers(days, state) {
  days.querySelectorAll("[part~='date']").forEach(cell => {
    const date = cell.date;
    if (!date) return;
    const year = date instanceof Date ? date.getFullYear() : date.year;
    const month = date instanceof Date ? date.getMonth() + 1 : date.month;
    const day = date instanceof Date ? date.getDate() : date.day;
    if (!year || !month || !day) return;
    const dateKey = [
      year,
      String(month).padStart(2, "0"),
      String(day).padStart(2, "0")
    ].join("-");
    const orders = state.entries.filter(entry => entry.date === dateKey);
    let markers = Array.from(cell.children)
      .find(child => child.classList.contains("calendar-order-markers"));
    if (!markers) {
      markers = document.createElement("div");
      markers.className = "calendar-order-markers";
      markers.style.cssText = "display:flex;flex-direction:column;gap:1px;width:100%;min-width:0";
      cell.append(markers);
    }
    const signature = JSON.stringify(orders);
    if (markers.dataset.signature === signature) return;
    markers.dataset.signature = signature;
    markers.replaceChildren();
    orders.forEach(order => {
      const marker = document.createElement("span");
      marker.className = "calendar-order-marker";
      marker.style.cssText = "display:flex;align-items:center;gap:3px;min-width:0;overflow:hidden;font-size:.65rem;line-height:1rem;font-weight:normal";
      marker.title = order.client;
      const dot = document.createElement("span");
      dot.className = `calendar-order-dot status-${order.status}`;
      dot.style.cssText = `display:inline-block;flex:0 0 6px;width:6px;height:6px;border-radius:50%;background-color:${statusColors[order.status] || "#7a7a7a"}`;
      dot.setAttribute("aria-hidden", "true");
      const label = document.createElement("span");
      label.className = "calendar-order-label";
      label.style.cssText = "display:block;min-width:0;overflow:hidden;text-overflow:ellipsis;white-space:nowrap";
      label.textContent = order.client;
      marker.append(dot, label);
      markers.append(marker);
    });
  });
}

function observeOrderMarkers(monthCalendar, state) {
  const days = monthCalendar.shadowRoot?.querySelector("#days-container");
  if (!days) {
    requestAnimationFrame(() => observeOrderMarkers(monthCalendar, state));
    return;
  }

  addStyles(monthCalendar.shadowRoot, "grid", monthCalendarGridStyles);
  const renderMarkers = () => renderOrderMarkers(days, state);
  state.renderMarkers = renderMarkers;
  renderMarkers();
  new MutationObserver(renderMarkers).observe(days, { childList: true, subtree: true });
  monthCalendar.addEventListener("month-changed", renderMarkers);
}

window.dashboardCalendar = {
  initialize(calendarElement, serializedEntries) {
    const monthCalendar = calendarElement.$.element;
    addStyles(calendarElement.shadowRoot, "calendar", monthCalendarStyles);
    monthCalendar.i18n = Object.assign({}, monthCalendar.i18n, calendarLocalization);

    const state = calendarStates.get(monthCalendar);
    if (state) {
      state.entries = JSON.parse(serializedEntries);
      state.renderMarkers?.();
      return;
    }

    const newState = { entries: JSON.parse(serializedEntries), renderMarkers: null };
    calendarStates.set(monthCalendar, newState);
    const initializeMarkers = () => {
      const afterUpdate = () => observeOrderMarkers(monthCalendar, newState);
      if (monthCalendar.updateComplete) {
        monthCalendar.updateComplete.then(afterUpdate);
      } else {
        requestAnimationFrame(afterUpdate);
      }
    };
    customElements.whenDefined("vaadin-month-calendar").then(initializeMarkers);
  }
};
