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
  //  Load a ListRecordSets using Account
  async loadRecordSetItems({ commit, state }, payload) {
    await this.$axios
      .get(process.env.backendUrl + '/recordsets/' + payload.HostedZoneId)
      .then((res) => {
        commit('loadRecordSetItems', res.data)
      })
      .catch(() => {})
  },
}
