import { EVENT_TYPE_LIFECYCLE_READY, EVENT_TYPE_UI_RESET_VIEW, EVENT_TYPE_LIFECYCLE_START } from '../constants';
// TODO 现有的主菜单的状态管理代码不便于维护和理解后续重构优化
const defaultMenus = [[], [], [], []];

const defaultSelectMenus = [];

export default {
  namespace: 'global',

  state: {
    // 全局 UI 状态
    ui: {
      mainMenu: false,
      userMenu: false,
      sideMenu: false,
      showUserSetting: false,
      loaded: [],
    },
    mainMenus: defaultMenus,
    selectMenus: [],
    activeFunction: null,
    functions: [],
    notices: [],
  },

  effects: {
    *start({ source }, { select }) {
      const session = yield select(({ login }) => ({ session: login.session }));
      source.postMessage({ type: EVENT_TYPE_LIFECYCLE_START, session }, '*');
    },
  },

  reducers: {
    setState(state, newState) {
      return {
        ...state,
        ...newState,
      };
    },
    hiddenUserSetting(state) {
      return {
        ...state,
        ui: {
          ...state.ui,
          showUserSetting: false,
        },
      };
    },
    showUserSetting(state) {
      return {
        ...state,
        ui: {
          ...state.ui,
          showUserSetting: true,
        },
      };
    },
    triggerMainMenu(state) {
      const ui = {
        ...state.ui,
        loaded: state.ui.loaded,
        mainMenu: !state.ui.mainMenu,
        sideMenu: false,
        userMenu: false,
      };
      return {
        ...state,
        ui: {
          ...ui,
        },
        mainMenus: defaultMenus,
        selectMenus: defaultSelectMenus,
      };
    },
    triggerUserMenu(state) {
      const ui = {
        ...state.ui,
        userMenu: !state.ui.userMenu,
        mainMenu: false,
        sideMenu: false,
      };
      return {
        ...state,
        ui: {
          ...ui,
        },
        mainMenus: defaultMenus,
        selectMenus: defaultSelectMenus,
      };
    },
    triggerSideMenu(state) {
      const ui = {
        ...state.ui,
        sideMenu: !state.ui.sideMenu,
        mainMenu: false,
      };
      return {
        ...state,
        ui: {
          ...ui,
        },
        mainMenus: defaultMenus,
        selectMenus: defaultSelectMenus,
      };
    },
    hidden(state) {
      return {
        ...state,
        ui: {
          sideMenu: false,
          mainMenu: false,
          loaded: state.ui.loaded,
        },
        mainMenus: defaultMenus,
        selectMenus: defaultSelectMenus,
      };
    },
    loaded(state, action) {
      const newState = {
        ...state,
      };
      newState.ui.loaded = [...state.ui.loaded, action.id];
      return newState;
    },
    openFunction(state, action) {
      const { functions } = state;
      if (functions.filter(f => f.id === action.function.id).length > 0) {
        return {
          ...state,
          activeFunction: `${action.function.id}`,
          ui: {
            ...state.ui,
            mainMenu: false,
          },
          mainMenus: defaultMenus,
          selectMenus: defaultSelectMenus,
        };
      } else {
        functions.push({ ...action.function, closable: true, loaded: false });
        return {
          ...state,
          ui: {
            ...state.ui,
            mainMenu: false,
          },
          mainMenus: defaultMenus,
          selectMenus: defaultSelectMenus,
          functions,
          activeFunction: `${action.function.id}`,
        };
      }
    },
    closeFunction(state, action) {
      let lastIndex;
      let { activeFunction } = state;
      state.functions.forEach((f, i) => {
        if (action.id === `${f.id}`) {
          lastIndex = i - 1;
        }
      });
      const functions = state.functions.filter(f => `${f.id}` !== action.id);
      if (`${state.activeFunction}` === action.id) {
        if (lastIndex >= 0) {
          activeFunction = `${functions[lastIndex].id}`;
        } else {
          activeFunction = null;
        }
      }

      return {
        ...state,
        ui: {
          ...state.ui,
          mainMenu: false,
        },
        mainMenus: defaultMenus,
        selectMenus: defaultSelectMenus,
        functions,
        activeFunction,
      };
    },
    activeFunction(state, action) {
      return {
        ...state,
        activeFunction: action.id,
      };
    },
    selectMenu(state, action) {
      const { select, level } = action;

      const newMenus = state.mainMenus.map((menus, index) => {
        if (index < level) {
          return menus;
        } else {
          return index === level ? select.childrens : [];
        }
      });
      const newSelectMenus = state.selectMenus.filter((m, index) => index < level);
      newSelectMenus.push(select.id);
      return {
        ...state,
        mainMenus: newMenus,
        selectMenus: newSelectMenus,
      };
    },
  },
  subscriptions: {
    setup({ dispatch }) {
      window.addEventListener(
        'message',
        event => {
          if (event.data.type === EVENT_TYPE_UI_RESET_VIEW) {
            dispatch({ type: 'hidden' });
          }
          if (event.data.type === EVENT_TYPE_LIFECYCLE_READY) {
            dispatch({ type: 'start', source: event.source });
          }
        },
        false
      );
    },
  },
};
