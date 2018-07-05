import { stringify } from 'qs';
import request from '../utils/request';

export async function accountLogin(params) {
  return request('/apis/reap-portal/logon', {
    method: 'POST',
    body: params,
  });
}

export async function menuTree() {
  return request('/apis/reap-portal/menus/tree', { method: 'GET' });
}

export async function menus(params) {
  return request(`/apis/reap-portal/menus?${stringify(params,{ skipNulls: true })}`, { method: 'GET' });
}

export async function functions() {
  return request('/apis/reap-portal/portal/functions', { method: 'GET' });
}

export async function createMenu(menu) {
  const url = !menu.parentId ? '/apis/reap-portal/menu' : `/apis/reap-portal/menu/${menu.parentId}`;
  return request(url, { method: 'POST', body: menu });
}

export async function deleteMenu(id) {
  return request(`/apis/reap-portal/menu/${id}`, { method: 'DELETE' });
}

export async function updateMenu(menu) {
  return request(`/apis/reap-portal/menu`, { method: 'PUT', body: menu });
}

export async function moveMenu(dragMenuId, targetMenuId, position) {
  return request(`/apis/reap-portal/menu/move/${dragMenuId}/${targetMenuId}/${position}`, { method: 'POST' });
}

export async function createUserSetting(userSetting) {
  return request('/apis/reap-portal/setting', { method: 'POST', body: userSetting });
}

export async function updateUserSetting(userSetting) {
  return request('/apis/reap-portal/setting', { method: 'PUT', body: userSetting });
}
