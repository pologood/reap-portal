import session from '../utils/session';
import feedback from '../utils/feedback';
import { accountLogin, createUserSetting, updateUserSetting } from '../services/api';
import { WELCOME_PAGE_FUNCTION_CODE } from '../constants';

const { notification: { error, success } } = feedback;
export default {
  namespace: 'login',
  state: {
    session: session.getSession() || {},
    result: undefined,
  },
  effects: {
    *login({ payload }, { call, put }) {
      const result = yield call(accountLogin, payload);
      yield put({ type: 'changeLoginStatus', result });
    },

    *setFavFunctions({ favFunctions }, { call, put, select }) {
      const userSetting = yield select(({ login }) => login.session.userSetting);
      const user = yield select(({ login }) => login.session.user);
      const api = userSetting && userSetting.id ? updateUserSetting : createUserSetting;
      const result = yield call(api, {
        ...userSetting,
        favFunctions: favFunctions.map(code => ({ functionCode: code })),
        userId: user.id,
      });
      if (result.success) {
        yield put({ type: 'updateUserSetting', userSetting: result.payload });
        success('设置常用功能成功.');
      } else {
        error(result);
      }
    },
    *setHomeFunction({ homeFunctionCode }, { call, put, select }) {
      const userSetting = yield select(({ login }) => login.session.userSetting);
      const user = yield select(({ login }) => login.session.user);
      const api = userSetting && userSetting.id ? updateUserSetting : createUserSetting;
      const result = yield call(api, {
        ...userSetting,
        homeFunctionCode: homeFunctionCode === WELCOME_PAGE_FUNCTION_CODE ? null : homeFunctionCode,
        userId: user.id,
      });
      if (result.success) {
        yield put({ type: 'updateUserSetting', userSetting: result.payload });
        success('自定义首页设置成功.');
      } else {
        error(result);
      }
    },
  },
  reducers: {
    updateUserSetting(state, { userSetting }) {
      const functions = state.session.user.roles.flatMap(r => r.functions);
      const homeFunction = functions.find(f => f.code === userSetting.homeFunctionCode);
      const favFunctions =
        userSetting.favFunctions &&
        userSetting.favFunctions.map(fav => {
          const func = functions.find(f => f.code === fav.functionCode);
          return {
            ...fav,
            function: func,
          };
        });
      const setting = { ...userSetting, homeFunction, favFunctions };
      const newSession = {
        ...state.session,
        userSetting: setting,
      };
      session.setSession(newSession);
      return {
        ...state,
        session: newSession,
      };
    },
    changeLoginStatus(state, { result }) {
      if (result.success) {
        session.setSession(result.payload);
        return {
          ...state,
          result,
          session: result.payload,
        };
      } else {
        return {
          ...state,
          result,
        };
      }
    },
    changeLogoutStatus(state) {
      session.cleanSession();
      return {
        ...state,
        session: {},
      };
    },
  },
};
