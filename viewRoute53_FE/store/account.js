export const state = () => ({
  accountItems: [],
  accountItem: [],

  gettersAccountItems: [],
})

export const mutations = {
  /* Load a Account Info */
  loadAccountItems(state, payload) {
    state.accountItems = []
    state.accountItems = payload
  },

  getAccountItem(state, payload) {
    state.accountItem = []
    state.accountItem = payload
  },
}

export const actions = {
  // 전체 회원 정보 조회
  async loadAccountItems({ commit, state }, payload) {
    await this.$axios
      .get(`http://localhost:6060/account`)
      .then((result) => {
        commit('loadAccountItems', result.data)
      })
      .catch(() => {
        // TODO: 에러 명을 v-alert으로 전달하자.
      })
  },

  // 단일 회원 정보 조회
  async getAccountItem({ commit, state }, payload) {
    await this.$axios
      .get(`http://localhost:6060/account/idx/` + payload.accountIdx)
      .then((result) => {
        commit('getAccountItem', result.data)
      })
      .catch(() => {})
  },

  // 회원 정보 등록
  async saveAccount({ commit, state }, payload) {
    await this.$axios
      .post('http://localhost:6060/account', payload)
      .then(() => {})
      .catch(() => {})
  },

  // 회원 정보 수정
  putAccount({ commit, state }, payload) {},

  // 회원 정보 삭제
  async deleteAccount({ commit, state }, payload) {
    await this.$axios
      .delete('http://localhost:6060/account/idx/' + payload.accountIdx)
      .then(() => {})
      .catch(() => {})
  },
}
