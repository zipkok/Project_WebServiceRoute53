export const state = () => ({
  recordsItems: [],
})

export const mutations = {
  /*
   * Load a ListRecordSets using Account
   */
  loadRecordSetItems(state, payload) {
    state.recordsItems = payload
  },
}

export const actions = {
  /*
   * Load a ListRecordSets using Account
   */
  loadRecordSetItems({ commit, state }, payload) {
    this.$axios
      // .delete 하고

      // .post 하는건 어때?

      // .get RecordSets 조회
      .get('http://localhost:6060/recordsets/' + payload.HostedZoneId)
      .then((res) => {
        commit('loadRecordSetItems', res.data)
      })
      .catch(() => {
        // TODO: 에러 명을 v-alert으로 전달하자.
      })
  },

  /*
   * Records Sets 조회
   */
}
