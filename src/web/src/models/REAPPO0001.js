import feedback from '../utils/feedback';
import { menuTree, menus, functions, createMenu, deleteMenu, updateMenu, moveMenu } from '../services/api';

const DEFAULT_PAGE_SIZE = 10;

const DEFAULT_PAGE_NUMBER = 0;
const { notification: { error } } = feedback;

function menusSpec(state) {
  return {
    parentId: state.selected && state.selected.id,
    size: state.page && state.page.size,
    page: state.page && state.page.number,
  };
}

export default {
  namespace: 'REAPPO0001',
  state: {
    menus: [],
    functions: [],
    page: {
      // 每页记录条数
      size: DEFAULT_PAGE_SIZE,
      // 当前页码
      number: DEFAULT_PAGE_NUMBER,
    },
    search: {
      name: null,
      level: null,
      functionCode: null,
    },
    selected: null,
    editModalVisiable: false,
    parentMenuList: [],
  },

  reducers: {
    setState(state, newState) {
      return {
        ...state,
        ...newState,
      };
    },
  },
  effects: {
    *moveMenu({ dragMenuId, targetMenuId, position }, { call, put }) {
      const result = yield call(moveMenu, dragMenuId, targetMenuId, position);
      if (result.success) {
        yield put({ type: 'menus', page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE });
        yield put({ type: 'menuTree' });
      } else {
        error(result);
      }
    },
    *select({ selected }, { put }) {
      if (selected) {
        if (selected.leaf !== 'Y') {
          yield put({
            type: 'menus',
            parentId: selected && selected.id,
            page: DEFAULT_PAGE_NUMBER,
            size: DEFAULT_PAGE_SIZE,
          });
          yield put({ type: 'setState', selected });
        }
      } else {
        yield put({
          type: 'menus',
          parentId: selected && selected.id,
          page: DEFAULT_PAGE_NUMBER,
          size: DEFAULT_PAGE_SIZE,
        });
        yield put({ type: 'setState', selected });
      }
    },
    // 查询菜单树
    *menuTree(action, { call, put }) {
      // 查询菜单树
      const result = yield call(menuTree);
      if (result.success) {
        yield put({ type: 'setState', menus: result.payload });
      } else {
        error(result);
      }
    },
    // 查询菜单列表
    *menus({ page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE }, { call, put, select }) {
      const state = yield select(({ REAPPO0001 }) => REAPPO0001);
      const params = {
        size,
        page,
        parentId: state.selected && state.selected.id,
        ...state.search,
      };
      const result = yield call(menus, params);
      if (result.success) {
        yield put({ type: 'setState', page: result.payload });
      } else {
        error(result);
      }
    },
    // 查询功能码列表
    *functions(action, { call, put }) {
      const result = yield call(functions);
      if (result.success) {
        yield put({ type: 'setState', functions: result.payload });
      } else {
        error(result);
      }
    },
    *create({ menu }, { call, select, put }) {
      const state = yield select(({ REAPPO0001 }) => REAPPO0001);
      const result = yield call(createMenu, { ...menu, parentId: state.selected && state.selected.id });
      yield put({ type: 'setState', editModalVisiable: false });
      if (result.success) {
        yield put({ type: 'menus', ...menusSpec(state) });
        yield put({ type: 'menuTree' });
      } else {
        error(result);
      }
    },
    *update({ menu }, { call, put, select }) {
      const state = yield select(({ REAPPO0001 }) => REAPPO0001);
      const result = yield call(updateMenu, menu);
      if (result.success) {
        yield put({ type: 'menus', ...menusSpec(state) });
        yield put({ type: 'menuTree' });
      } else {
        error(result);
      }
    },
    // 删除菜单
    *delete({ id }, { call, put, select }) {
      const result = yield call(deleteMenu, id);
      if (result.success) {
        const state = yield select(({ REAPPO0001 }) => REAPPO0001);
        if (state.selected && state.selected.key === id) {
          yield put({ type: 'setState', selected: null });
          yield put({ type: 'menus', ...menusSpec(state), parentId: null });
        } else {
          yield put({ type: 'menus', ...menusSpec(state) });
        }
        yield put({ type: 'menuTree' });
      } else {
        error(result);
      }
    },
  },
  subscriptions: {
    setup({ dispatch, history }) {
      // 页面初始化时进行第一次查询
      history.listen(({ pathname }) => {
        if (pathname === '/REAPPO0001') {
          dispatch({ type: 'menuTree' });
          dispatch({ type: 'menus', page: DEFAULT_PAGE_NUMBER, size: DEFAULT_PAGE_SIZE });
          dispatch({ type: 'functions' });
        }
      });
    },
  },
};
