export const state = () => ({
  hostedzoneId: '123',

  recordsItems: ['asd', 'asdd'],
})

export const mutations = {
  loadRecordSetItems(state, payload) {
    state.recordsItems = payload
  },
}

export const actions = {
  loadRecordSetItems({ commit, state }, payload) {
    this.$axios
      .get(`http://localhost:6060/recordsets/Z0338876FY905DAOM13I`)
      .then((res) => {
        console.log('WOWOWOWO')
        commit('loadRecordSetItems', res.data)
      })
      .catch(() => {})
  },
}
