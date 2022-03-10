export const state = () => ({
  accountItems: [],
})

export const mutations = {
  /*
   * Load a Account Info
   */
  loadAccountItems(state, payload) {
    // accountItems 초기화
    state.accountItems = []

    // accountItems 가공해서 autocomplete의 List로 사용
    for (const idx in payload) {
      const getterAccount =
        payload[idx].hostedZoneName +
        ' / ' +
        payload[idx].team +
        ' / ' +
        payload[idx].accountName
      state.accountItems.push(getterAccount)
    }
  },
}

export const actions = {
  /*
   * Account Info
   */
  loadAccountItems({ commit, state }, payload) {
    this.$axios
      .get(`http://localhost:6060/account`)
      .then((res) => {
        commit('loadAccountItems', res.data)
      })
      .catch(() => {
        // TODO: 에러 명을 v-alert으로 전달하자.
      })
  },
}
